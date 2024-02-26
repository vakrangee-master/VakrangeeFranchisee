package in.vakrangee.core.commongui;

public class ImageGIFDto {

    private String title;
    private int imageViewId;

    public ImageGIFDto(String title, int imageViewId) {
        this.title = title;
        this.imageViewId = imageViewId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageViewId() {
        return imageViewId;
    }

    public void setImageViewId(int imageViewId) {
        this.imageViewId = imageViewId;
    }
}
