package com.mapa.mechame.mechame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,LocationListener {

    private static com.google.android.gms.location.LocationListener locationListener;
    private GoogleMap mMap;
    String endereco;
    String cidade;
    String pais;
    double latitude;
    double longitude;
    private AlertDialog alerta;
    private GoogleApiClient mClient;
    private LocationRequest mLocationRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);


        //Criando o Cliente se Localização do Google
        conectarClienteLocalizacao();

        final TextView txtLocal = (TextView) findViewById(R.id.txtBuscaLocal);
        ImageButton btnLembretes = (ImageButton) findViewById(R.id.btnLembretes);
        ImageButton btnConfig = (ImageButton) findViewById(R.id.btnConfig);
        ImageView btnBuscar = (ImageView) findViewById(R.id.btnBuscaLocal);
        //Caso o mapa não esteja correto
        setUpMapIfNeeded();

        //Click do MAPA
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            public void onMapClick(LatLng latLng){
                latitude=latLng.latitude;
                longitude=latLng.longitude;
                criarLembrete();
            }});

        //Click no MARCADOR
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            public boolean onMarkerClick(Marker marker){
                LatLng lat = marker.getPosition();
                mostraLocal(lat.latitude, lat.longitude);
                marker.setTitle(endereco);
                marker.setSnippet(cidade+" "+pais );
            return false;
            }});


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Localizador localizar = new Localizador();
                LatLng local;
                local = localizar.getCoordenada(txtLocal.getText().toString(),MapsActivity.this);
                mostraProcuraFeita(local);
            }
        });
        //Click do botão lembrete
        btnLembretes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] lembretes = {"Lembrete1", "Lembrete2"};
                int layout = android.R.layout.simple_list_item_1;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapsActivity.this, layout, lembretes);
                ListView lista = new ListView(MapsActivity.this);
                lista.setAdapter(adapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Lembretes");
                builder.setView(lista);
                builder.setNegativeButton("Voltar",null);
                alerta = builder.create();
                alerta.show();
            }
        });
        //CLick do botão configuração
        btnConfig.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View v) {
                LayoutInflater li = getLayoutInflater();
                RelativeLayout parentLayout = (RelativeLayout)findViewById(R.id.config);
                final View view;
                view =  li.inflate(R.layout.configuracoes,parentLayout, true);
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Configurações");
                builder.setView(view);
                builder.setNegativeButton("Voltar",null);
                builder.setPositiveButton("Sair do Me chame",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alerta = builder.create();
                alerta.show();

            }});
    }//Fim do OnCreate

    private void conectarClienteLocalizacao() {
        mClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        mClient.connect();
    }






        @Override
    protected void onResume() {
        super.onResume();



    }
    // Metodo para mostrar o mapa na tela !!!
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }
    //Metodo para centralizar o mapa no local desejado
    public void centralizaNo(LatLng local){
        GoogleMap mapa = mMap;
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 17));
        mostraLocal(local.latitude,local.longitude);
        //Adicionando o marcador
         mMap.addMarker(new MarkerOptions().title(endereco).snippet(cidade + " " + pais).position(new LatLng(local.latitude, local.longitude)));



    }

    private void mostraProcuraFeita(LatLng local){
        GoogleMap mapa = mMap;
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 17));
    }

    //Converter latitude em Nome de rua com o GEOCODER
    public List<Address> mostraLocal(double localizacaoLat, double localizacaoLong) {
        try {
            List<Address> addresses;
            Geocoder geocoder = new Geocoder(this);
                addresses = geocoder.getFromLocation(localizacaoLat ,localizacaoLong, 1);
                 endereco = addresses.get(0).getAddressLine(0);
                 cidade = addresses.get(0).getAddressLine(1);
                 pais = addresses.get(0).getAddressLine(2);
                return addresses;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Inflar VIEW e mostrar no ALert
    public void criarLembrete(){
        LayoutInflater li = getLayoutInflater();
        RelativeLayout parentLayout = (RelativeLayout)findViewById(R.id.criar_lembrete);
        final View view;
        view =  li.inflate(R.layout.criar_lembrete,parentLayout, true);
        //Criar Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Criar Lembrete");
        builder.setView(view);
        builder.setNegativeButton("Cancelar",null);
        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView tituloLembrete = (TextView) view.findViewById(R.id.txtDescricao);
                TextView diametro = (TextView) view.findViewById(R.id.txtDiametro);

                if(validacao(tituloLembrete.getText().toString(),diametro.getText().toString()))
                {
                    double raio = Double.parseDouble(diametro.getText().toString());
                    mMap.addMarker(new MarkerOptions().title(endereco)
                                                      .snippet(cidade + " " + pais)
                                                      .position(new LatLng(latitude, longitude))
                                                      .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)));
                    LembreteDAO dao = new LembreteDAO();
                    dao.lembreteToJson(tituloLembrete.getText().toString(), raio, true, latitude, longitude);
                    Toast.makeText(MapsActivity.this, "Lembrete criado com Sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alerta = builder.create();
        alerta.show();
    }




    //Conexão do Serviço de Localização do Google
    @Override
    public void onConnected(Bundle bundle) {
        Location local = LocationServices.FusedLocationApi.getLastLocation(mClient);
        if(local !=null){

            centralizaNo(new LatLng(local.getLatitude(),local.getLongitude()));


        }




       // mudancaLocal();


    }



    private void mudancaLocal(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLocationRequest,MapsActivity.locationListener);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    private boolean validacao(String titulo ,String raio){
        String mensagem = "";

        if(titulo.equals("")) {
            mensagem += "\nDigite o Título";
        }

        if(raio.equals(""))
            mensagem+="\nDigite o Raio";


        if (!mensagem.equals("")) {
            AlertDialog alerta;
            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            builder.setTitle("Atenção !");
            builder.setNegativeButton("Fechar",null);
            builder.setMessage(mensagem);
            alerta = builder.create();
            alerta.show();
        }

        return mensagem.equals("");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Log","LATITUDE: "+location.getLatitude()+" "+location.getLongitude());
        Log.i("Log","SPEED: "+location.getSpeed());
        Log.i("Log","TIME: "+DateFormat.getTimeInstance().format(new Date()));
        centralizaNo(new LatLng(location.getLatitude(),location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("DESLIGADO","GPS Desligado: ");



    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("DESLIGADO","GPS Desligado: ");
    }
}
