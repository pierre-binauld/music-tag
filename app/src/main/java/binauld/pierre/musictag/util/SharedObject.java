package binauld.pierre.musictag.util;

import java.util.List;
import java.util.Map;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.factory.LibraryComponentFactory;
import binauld.pierre.musictag.item.AudioFile;
import binauld.pierre.musictag.tag.Id3Tag;

public abstract class SharedObject {

    private static List<LibraryComponent> components;
    private static LibraryComponentFactory componentFactory;
    private static Map<AudioFile, Id3Tag> id3Tags;

    public static void provideComponents(List<LibraryComponent> components) {
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

    public static Map<AudioFile, Id3Tag> getId3Tags() {
        return id3Tags;
    }

    public static void provideId3Tags(Map<AudioFile, Id3Tag> id3Tags) {
        SharedObject.id3Tags = id3Tags;
    }
}
