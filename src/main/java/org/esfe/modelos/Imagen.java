package org.esfe.modelos;

import jakarta.persistence.*;
import java.util.Base64;

@Entity
@Table(name = "imagenes")
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(name = "imagen_bytes")
    //@Column(name = "imagen_bytes", columnDefinition = "VARBINARY(MAX)")
    private byte[] bytesArrayImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    private DestinoTuristico destinoTuristico;

    // --- Getter y Setter normales ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public byte[] getBytesArrayImage() { return bytesArrayImage; }
    public void setBytesArrayImage(byte[] bytesArrayImage) { this.bytesArrayImage = bytesArrayImage; }

    public DestinoTuristico getDestinoTuristico() { return destinoTuristico; }
    public void setDestinoTuristico(DestinoTuristico destinoTuristico) { this.destinoTuristico = destinoTuristico; }

    // --- Getter para Base64 ---
    @Transient
    public String getBase64Image() {
        if (bytesArrayImage != null && bytesArrayImage.length > 0) {
            return Base64.getEncoder().encodeToString(bytesArrayImage);
        }
        return null;
    }
}