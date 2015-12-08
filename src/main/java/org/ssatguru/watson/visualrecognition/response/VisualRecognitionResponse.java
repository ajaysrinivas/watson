
package org.ssatguru.watson.visualrecognition.response;

import java.util.List;

/**
 *
 * @author Matti Tahvonen
 */
public class VisualRecognitionResponse {
    
    private List<ImageData> images;

    public List<ImageData> getImages() {
        return images;
    }

    public void setImages(List<ImageData> images) {
        this.images = images;
    }

}
