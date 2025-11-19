package org.esfe.modelos;

public class ImagenDTO {
    private Integer id;
    private String base64Image;

    public ImagenDTO(Integer id, String base64Image) {
        this.id = id;
        this.base64Image = base64Image;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getBase64Image() { return base64Image; }
    public void setBase64Image(String base64Image) { this.base64Image = base64Image; }
}