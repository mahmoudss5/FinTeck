package BankSystem.demo.Aspect.Security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
// this annotation will be used to check if the user is an admin
public @interface RequiresAdmin {
    
}
