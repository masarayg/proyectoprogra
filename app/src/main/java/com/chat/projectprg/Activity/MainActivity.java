package com.chat.projectprg.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.projectprg.AdapterMensaje;
import com.chat.projectprg.Entidades.MensajeEnviar;
import com.chat.projectprg.Entidades.MensajeRecibir;
import com.chat.projectprg.Entidades.Usuario;
import com.chat.projectprg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MainActivity extends AppCompatActivity {


    //Funciones private
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar, cerrarSesion;
    private AdapterMensaje adapter;

    //Objetos para base de datos
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private String NOMBRE_USUARIO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nombre = findViewById(R.id.nombre);
        rvMensajes = findViewById(R.id.rvMensajes);
        txtMensaje = findViewById(R.id.txtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        cerrarSesion = (Button) findViewById(R.id.cerrarSesion);


        //Inicializar componentes del Database
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("chatV2"); //Sala del Chat version 2
        mAuth = FirebaseAuth.getInstance();

        adapter = new AdapterMensaje(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               databaseReference.push().setValue(new MensajeEnviar(txtMensaje.getText().toString(),NOMBRE_USUARIO,"1", ServerValue.TIMESTAMP));
               txtMensaje.setText("");
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                returnLogin();
            }
        });

        //Despliegue de texto
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {

         //Agregando datos de Db a nuestra lista
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MensajeRecibir m = dataSnapshot.getValue(MensajeRecibir.class);
                    adapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //despliegue sin afectar el texto en pantalla
     private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);

     }

     //envio de imagen
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageReference = storage.getReference("Imagenes_chat");// storage imagenes
    }*/
   @Override
   protected  void onResume(){
       super.onResume();
       FirebaseUser currentUser = mAuth.getCurrentUser();
       if(currentUser!=null){
           btnEnviar.setEnabled(false);
           DatabaseReference reference = database.getReference("Usuario/"+currentUser.getUid());
           reference.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   Usuario usuario = dataSnapshot.getValue(Usuario.class);
                   NOMBRE_USUARIO = usuario.getNombre();
                   nombre.setText(NOMBRE_USUARIO);
                   btnEnviar.setEnabled(true);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }else{
        returnLogin();
       }
   }

   private  void returnLogin(){
       startActivity(new Intent(MainActivity.this, LoginActivity.class) );
       finish();
   }
}
