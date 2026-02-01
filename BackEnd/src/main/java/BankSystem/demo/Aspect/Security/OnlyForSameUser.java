package BankSystem.demo.Aspect.Security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // this annotation can be used on methods
@Retention(RetentionPolicy.RUNTIME) // this annotation will be available at runtime
// this annotation will be used to check if the user is the same as the one trying to access the resource
public @interface OnlyForSameUser {


}
