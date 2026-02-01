package BankSystem.demo.Aspect.Auditing;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
// this annotation will be used to log audit information for the annotated methods
public @interface AuditLog {
}
