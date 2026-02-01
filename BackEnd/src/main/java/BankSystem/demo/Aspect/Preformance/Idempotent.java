package BankSystem.demo.Aspect.Preformance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
//this annottain will be used to make the method idempotent (prevent duplicate requests)          
public @interface Idempotent {
}
