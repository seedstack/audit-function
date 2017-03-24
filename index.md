---
title: "Audit"
repo: "https://github.com/seedstack/audit-addon"
author: Yves DAUTREMAY
description: "The Audit add-on provides the ability to trace who does what in your application."
tags:
    - "audit"
    - "trace"
    - "trail"
zones:
    - Addons
menu:
    AuditAddon:
        weight: 10
---

The Audit add-on provides the ability to trace who does what in your application.

# Dependencies

The core functionality of the audit add-on is in the following dependency: 

{{< dependency g="org.seedstack.addons.audit" a="audit-core" >}}

To use the Logback audit writer, add the following dependency:

{{< dependency g="org.seedstack.addons.audit" a="audit-logback" >}}

# Concepts

The Audit allows you to trace somewhere (log file for example) each time a user executes a portion of code. You can 
record a message and get access to information like the date,  the connected user or the application concerned. 

Following is the model of what is given to the object in charge of writing the audit:

![audit model]({{< baseUrl >}}puml/business/business-api-domain-audit.png)

 - AuditEvent: Main object passed to the trail writer. It contains the date of the audit and the accompanying message. 
 It also has the trail
 - Trail: A trail represents several events occurring in the same execution. All events of a trail have the same 
 Initiator and are requested on the same Host.
 - Initiator: Represents the user triggering the audit. It contains his id and his name as well has his Address.
 - Host: represents the application on which the audit is made. It contains the name and id of the application as 
 well as its address.
 - Address: Contains Network information if available: ip address and dns name.

# Configuration

{{% config p="audit" %}}
```yaml
audit:
  # The set of configured trail writers classes
  writers: (Set<Class<? extends TrailWriter>>)
  
  # The set of configured trail exception handlers
  exceptionHandlers: (Set<Class<? extends TrailExceptionHandler<?>>>)
```
{{% /config %}}   

# Usage

## The @Audited Annotation

You can mark a method with the annotation *@Audited* so the framework will automatically audit this the execution of 
the method:

```java
public class SomeClass {
    @Audited(
        messageBefore = "Doing critical work with parameter ${args[0]}...",
        messageAfter = "Done critical work with result: ${result}",
        messageOnException = "Error doing critical work !")
    public String doCriticalWork(String someString) {
        return "result: " + someString;
    }
}
```


There are 3 attributes you can define, the first being mandatory :

* **messageAfter**: the message to trace when the method has been executed. Can be an EL expression with properties 
"args" for the method arguments and "result" for the return. For example: 
`messageAfter = "the argument is \${args[0].someMethod()} and the result says \${result.say()}"`
* **messageBefore**: optionally, you can define a message to be traced just before executing the method. 
Can be an EL expression but only arguments are available.
* **messageOnException**: define a generic message when an unhandled exception occurs in the execution of the method. 
If a handler handles the exception, this message is ignored. Can be an EL expression, the exception is available via 
property "exception". For instance: `messageOnException = "kaboom : \${exception.getMessage()}"`

## AuditService
You can programmatically write a trail by injecting the AuditService. First create a new Trail that you can reuse later. 
It will be initialized automatically with the current Host and Initiator. Then trail as many messages as required with 
the given Trail.

```java
public class SomeClass {
    @Inject
    private AuditService auditService;
    
    public void someMethod() {
        Trail trail = auditService.createTrail();
        String message = "dummy";
        auditService.trail(message, trail);
    } 
}
```
    
## TrailWriter
A trail writer effectively writes each message and its trail (an AuditEvent). The framework brings a writer based on 
Logback that can write on a file, in the console... named LogbackTrailWriter

You can implement your own TrailWriter. For example :

```java
public class SysoutTrailWriter implements TrailWriter{
    public void writeEvent(AuditEvent auditEvent) {
        System.out.println(auditEvent.getDate() + " " + auditEvent.getMessage()
            + " [" + auditEvent.getTrail().getInitiator().getId + "]");
    }
}
```
    
## TrailExceptionHandler
A TrailExceptionHandler is used in conjunction with the @Audited annotation. When the annotated method throws an exception, 
if a handler is able to handle the exception, it will create a String describing it, being the message that will be trailed. 
The framework brings an exception handler for AuthorizationException.
You can implement your own handler

```java
public class BusinessTrailExceptionHandler implements
        TrailExceptionHandler<MyBusinessException> {

    public String describeException(MyBusinessException e){
        return "My business description to trail";
    }
}
```

# Logback trail writer

If you choose to use the LogbackTrailWriter, you must specify the `audit.logPattern` configuration property with the 
pattern that will be used to write each message. It can be an EL expression, with the following properties available:

* event: the `AuditEvent` object.
* trail: the trail. Could also be accessed via `event.getTrail()`.
* initiator: the initiator. Could also be accessed via `event.getTrail().getInitiator()`.
* host: the application host. Could also be accessed via `event.getTrail().getHost()`.
 
Here is an example of pattern:

```yaml
audit:
  writers: org.seedstack.audit.logback.LogbackTrailWriter
  logPattern: 'At \${event.getFormattedDate("yyyy/MM/dd HH:mm:ss.SSS")} user \${initiator.getId()} requested application \${host.getName()} : \${event.getMessage()}'  
```

Note the escaping of dollar signs, as we don't want them to be resolved as configuration macros. Then configure 
logback to add the appender and logger in the `logback.xml` file:

```xml
<configuration>
  <appender name="AUDIT_APPENDER" class="ch.qos.logback.core.ConsoleAppender">
      <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
        <layout class="org.seedstack.audit.logback.AuditLogbackLayout" />
      </encoder>
    </appender>
  <logger name="AUDIT_LOGGER" additivity="false">
    <appender-ref ref="AUDIT_APPENDER" />
  </logger>
</configuration>
```
    
Note that you can choose any appender that holds an encoder (ConsoleAppender, FileAppender...) as long as you define 
the right layout class.
