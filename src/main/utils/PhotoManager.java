package utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class PhotoManager {
    public static Map<String, Image> imagesCache = new HashMap<>();

    public static ImageView getView(String url) {
        Image photo = imagesCache.get(url);
        ImageView view;
        if (photo == null) {
            photo = new Image(url);
        }
        view = new ImageView(photo);
        view.setFitHeight(20);
        view.setFitWidth(20);
        return view;
    }
}
