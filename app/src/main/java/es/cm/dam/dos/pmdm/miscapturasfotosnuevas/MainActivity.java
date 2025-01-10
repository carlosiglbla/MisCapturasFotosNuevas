package es.cm.dam.dos.pmdm.miscapturasfotosnuevas;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public final int CAPTURA=0;
    private boolean HAY_PERMISOS=false;
    private Uri fotoUri;
    private ActivityResultLauncher<Uri> capturaImagenLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar el launcher para capturar imágenes
        capturaImagenLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        Toast.makeText(this, "Foto guardada en: " + fotoUri, Toast.LENGTH_LONG).show();
                        mostrarImagen();
                    } else {
                        Toast.makeText(this, "Error al capturar la imagen", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        findViewById(R.id.button).setOnClickListener(v -> hacerFoto());
    }

    private void hacerFoto(){
        // Crear entrada en MediaStore para la imagen
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "JPEG_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        fotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (fotoUri != null) {
            capturaImagenLauncher.launch(fotoUri);
        } else {
            Toast.makeText(this, "Error al preparar almacenamiento para la foto", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarImagen() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fotoUri);
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(this, "Error al mostrar la imagen", Toast.LENGTH_SHORT).show();
        }
    }


}