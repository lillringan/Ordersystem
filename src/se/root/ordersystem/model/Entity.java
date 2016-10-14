package se.root.ordersystem.model;

/**
 * The Class Entity - here we use factory Pattern for the classes in model.
 * Why? each class in the model package has these variables.
 */
public abstract class Entity {
    protected String id;
    protected boolean isActive;
}
