package binauld.pierre.musictag.util;

import java.util.List;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.factory.LibraryComponentFactory;

public abstract class SharedObject {

    private static List<LibraryComponent> components;
    private static LibraryComponentFactory componentFactory;

    public static void provideComponent(List<LibraryComponent> components) {
        SharedObject.components = components;
    }

    public static List<LibraryComponent> getComponents() {
        return components;
    }

    public static void provideComponentFactory(LibraryComponentFactory componentFactory) {
        SharedObject.componentFactory = componentFactory;
    }

    public static LibraryComponentFactory getComponentFactory() {
        return componentFactory;
    }
}
