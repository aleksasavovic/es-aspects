package asavovic.courseProject.aspects;

import asavovic.courseProject.entities.dto.ProductToAdd;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AddProductValidationAspect {

    @Before("execution(* asavovic.courseProject.controllers.CartController.addProductToCart(..))")
    public void validatieProductsToAdd(JoinPoint joinPoint) {
        Object object = joinPoint.getArgs()[1];
        if (object != null) {
            ProductToAdd productToAdd = (ProductToAdd) object;
            if (productToAdd.getAmountToAdd() <= 0)
                throw new IllegalArgumentException("amount to add has to be positive");
            if (productToAdd.getId() < 0)
                throw new IllegalArgumentException("productId has to be positive");
        } else throw new IllegalArgumentException("null value not allowed");
    }
}
