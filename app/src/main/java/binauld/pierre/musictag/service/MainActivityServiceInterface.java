//package binauld.pierre.musictag.service;
//
//
//import android.content.SharedPreferences;
//import android.content.res.Resources;
//
//import java.io.File;
//import java.util.List;
//
//import binauld.pierre.musictag.R;
//import binauld.pierre.musictag.activities.MainActivity;
//import binauld.pierre.musictag.composite.LibraryComponent;
//import binauld.pierre.musictag.composite.LibraryComposite;
//import binauld.pierre.musictag.composite.LoadingState;
//import binauld.pierre.musictag.decoder.BitmapDecoder;
//import binauld.pierre.musictag.decoder.ResourceBitmapDecoder;
//import binauld.pierre.musictag.factory.LibraryComponentFactory;
//import binauld.pierre.musictag.helper.LibraryComponentFactoryHelper;
//import binauld.pierre.musictag.item.Item;
//import binauld.pierre.musictag.visitor.impl.MaxChildrenCountExtractor;
//import binauld.pierre.musictag.wrapper.FileWrapper;
//import binauld.pierre.musictag.wrapper.jaudiotagger.JAudioTaggerWrapper;
//
//public class MainActivityServiceInterface implements MainActivity.LibraryServiceInterface {
//    private Resources res;
//    private SharedPreferences sharedPrefs;
//
//    private LibraryComposite composite;
//    private LibraryComponentFactory componentFactory;
//
//    public MainActivityServiceInterface(Resources res, SharedPreferences sharedPrefs) {
//        this.res = res;
//        this.sharedPrefs = sharedPrefs;
//
//        // Init default decoder
//        BitmapDecoder defaultArtworkBitmapDecoder = new ResourceBitmapDecoder(res, R.drawable.list_item_placeholder);
//
//        // Init Wrapper
//        FileWrapper wrapper = new JAudioTaggerWrapper();
//
//        // Init factory
//        componentFactory = LibraryComponentFactoryHelper.buildFactory(res, wrapper, defaultArtworkBitmapDecoder);
//
//        // Init composite
//        reset();
//    }
//
////    public void reset() {
////        composite = null;
////
////        String sourceFolder = sharedPrefs.getString(
////                res.getString(R.string.source_folder_preference_key),
////                res.getString(R.string.source_folder_preference_default));
////
////        LibraryComposite composite = componentFactory.buildComposite(new File(sourceFolder), null);
////        switchComposite(composite);
////    }
//
//
////    public void switchComposite(LibraryComposite composite) {
////        this.composite = composite;
////    }
//
//
////    @Override
////    public boolean backToParentComposite() {
////        LibraryComposite parent = composite.getParent();
////        if (null == parent) {
////            return false;
////        } else {
////            composite = parent;
////            return true;
////        }
////    }
//
//    @Override
//    public int getComponentMaxChildrenCount() {
//        Item item = composite.getItem();
//
//        MaxChildrenCountExtractor maxChildrenCountExtractor = new MaxChildrenCountExtractor();
//
//        item.accept(maxChildrenCountExtractor);
//
//        return maxChildrenCountExtractor.getListMaxSize();
//    }
//
//    @Override
//    public int getCurrentProgress() {
//        return composite.size() + composite.getInvalidComponentCount();
//    }
//
//    @Override
//    public LoadingState getCurrentLoadingState() {
//        return composite.getState();
//    }
//
//    @Override
//    public boolean hasParent() {
//        return composite.getParent() != null;
//    }
//
//    @Override
//    public String getComponentName() {
//        return composite.getItem().getPrimaryInformation();
//    }
//
//    @Override
//    public List<LibraryComponent> getCurrentComponentList() {
//        composite.getChildren().pull();
//        return composite.getChildren();
//    }
//
//    @Override
//    public void loadCurrentComposite(boolean drillDown, Runnable callback) {
//        asyncLoad(composite, drillDown, callback);
//    }
//}
