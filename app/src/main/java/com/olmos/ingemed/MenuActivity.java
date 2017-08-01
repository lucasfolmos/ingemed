package com.olmos.ingemed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.olmos.ingemed.adapter.TratamientoAdapter;
import com.olmos.ingemed.interfaces.ITratamientos;
import com.olmos.ingemed.model.TratamientoObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MenuActivity extends AppCompatActivity implements ITratamientos {

    TratamientoAdapter tratamientoAdapter;
    TratamientoObject selectedTratamiento;

    long capacitivoActual;
    long resistivoActual;
    boolean esCapacitivoActual;

    @BindView(R.id.activity_menu_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_menu_text_resistivo)
    TextView activityMenuTextResistivo;
    @BindView(R.id.activity_menu_text_capacitivo)
    TextView activityMenuTextCapacitivo;
    @BindView(R.id.activity_menu_tiempo_resistivo)
    TextView activityMenuTiempoResistivo;
    @BindView(R.id.activity_menu_resistivo_up)
    ImageView activityMenuResistivoUp;
    @BindView(R.id.activity_resistivo_down)
    ImageView activityResistivoDown;
    @BindView(R.id.activity_menu_tiempo_capacitivo)
    TextView activityMenuTiempoCapacitivo;
    @BindView(R.id.activity_menu_capacitivo_up)
    ImageView activityMenuCapacitivoUp;
    @BindView(R.id.activity_menu_capacitivo_down)
    ImageView activityMenuCapacitivoDown;
    @BindView(R.id.activity_menu_valores_defecto)
    Button activityMenuValoresDefecto;
    @BindView(R.id.activity_menu_orden)
    Button activityMenuOrder;
    @BindView(R.id.activity_menu_grabar)
    Button activityMenuGrabar;
    @BindView(R.id.activity_menu_ok)
    Button activityMenuOk;
    @BindView(R.id.activity_menu_detalle_tratamiento)
    TextView activityMenuDetalleTratamiento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        //Initialize data
        RealmResults<TratamientoObject> tratamientoObjects = getDataFromRealm();
        if (tratamientoObjects == null || tratamientoObjects.isEmpty()){
            setInitialData();
        }

        setUpRecycler();
    }

    private void setInitialData() {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Delete all items

                TratamientoObject tratamientoObject = new TratamientoObject();
                tratamientoObject.setName("CELULITIS");
                tratamientoObject.setMilisecondsCapacitivo(60 * 1000 * 4);
                tratamientoObject.setMilisecondsResistivo(60 * 1000 * 3);
                tratamientoObject.setDefaultMilisecondsResistivo(60 * 1000 * 2);
                tratamientoObject.setDefaultMilisecondsCapacitivo(60 * 1000 * 2);
                tratamientoObject.setCapacitivoPrimero(false);

                TratamientoObject tratamientoObject1 = new TratamientoObject();
                tratamientoObject1.setName("TONIFICACIÓN");
                tratamientoObject1.setMilisecondsCapacitivo(60 * 1000 * 15);
                tratamientoObject1.setMilisecondsResistivo(60 * 1000 * 2);
                tratamientoObject1.setDefaultMilisecondsResistivo(60 * 1000 * 2);
                tratamientoObject1.setDefaultMilisecondsCapacitivo(60 * 1000 * 2);
                tratamientoObject1.setCapacitivoPrimero(true);

                TratamientoObject tratamientoObject2 = new TratamientoObject();
                tratamientoObject2.setName("FLACIDEZ");
                tratamientoObject2.setMilisecondsCapacitivo(60 * 1000 * 8);
                tratamientoObject2.setMilisecondsResistivo(60 * 1000 * 10);
                tratamientoObject2.setDefaultMilisecondsResistivo(60 * 1000 * 2);
                tratamientoObject2.setDefaultMilisecondsCapacitivo(60 * 1000 * 2);
                tratamientoObject2.setCapacitivoPrimero(false);

                TratamientoObject tratamientoObject3 = new TratamientoObject();
                tratamientoObject3.setName("MODELAR");
                tratamientoObject3.setMilisecondsCapacitivo(60 * 1000 * 2);
                tratamientoObject3.setMilisecondsResistivo(60 * 1000 * 5);
                tratamientoObject3.setDefaultMilisecondsResistivo(60 * 1000 * 2);
                tratamientoObject3.setDefaultMilisecondsCapacitivo(60 * 1000 * 2);
                tratamientoObject3.setCapacitivoPrimero(false);

                realm.copyToRealm(tratamientoObject);
                realm.copyToRealm(tratamientoObject1);
                realm.copyToRealm(tratamientoObject2);
                realm.copyToRealm(tratamientoObject3);
            }
        });
    }

    private void grabarNuevoTiempos() {
        Realm realm = Realm.getDefaultInstance();
        final String name = selectedTratamiento.getName();
        final long defCapa = selectedTratamiento.getDefaultMilisecondsCapacitivo();
        final long defResist = selectedTratamiento.getDefaultMilisecondsResistivo();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TratamientoObject tratamientoObject = new TratamientoObject();
                tratamientoObject.setName(name);
                tratamientoObject.setDefaultMilisecondsCapacitivo(defCapa);
                tratamientoObject.setDefaultMilisecondsResistivo(defResist);
                tratamientoObject.setMilisecondsResistivo(resistivoActual);
                tratamientoObject.setMilisecondsCapacitivo(capacitivoActual);
                tratamientoObject.setCapacitivoPrimero(esCapacitivoActual);

                realm.copyToRealmOrUpdate(tratamientoObject);
            }
        });
    }

    @OnClick({R.id.activity_menu_resistivo_up, R.id.activity_resistivo_down, R.id.activity_menu_capacitivo_up, R.id.activity_menu_capacitivo_down, R.id.activity_menu_valores_defecto, R.id.activity_menu_orden, R.id.activity_menu_grabar, R.id.activity_menu_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_menu_resistivo_up:
                btnTiempoUpResistivo();
                break;
            case R.id.activity_resistivo_down:
                btnTiempoDownResistivo();
                break;
            case R.id.activity_menu_capacitivo_up:
                btnTiempoUpCapacitivo();
                break;
            case R.id.activity_menu_capacitivo_down:
                btnTiempoDownCapacitivo();
                break;
            case R.id.activity_menu_valores_defecto:
                activityMenuTiempoResistivo.setText(formatSecondsinScreen(selectedTratamiento.getDefaultMilisecondsResistivo()));
                activityMenuTiempoCapacitivo.setText(formatSecondsinScreen(selectedTratamiento.getDefaultMilisecondsCapacitivo()));
                break;
            case R.id.activity_menu_orden:
                esCapacitivoActual = !esCapacitivoActual;
                if(esCapacitivoActual) {
                    activityMenuTextResistivo.setText("CAPACITIVO");
                    activityMenuTextCapacitivo.setText("RESISTIVO");
                } else {
                    activityMenuTextResistivo.setText("RESISTIVO");
                    activityMenuTextCapacitivo.setText("CAPACITIVO");
                }
                break;
            case R.id.activity_menu_grabar:
                grabarNuevoTiempos();
                break;
            case R.id.activity_menu_ok:
                clickOK();
                break;
        }
    }

    private void clickOK(){
        Intent intent = new Intent();
        intent.putExtra(MainActivity.BUNDLE_NOMBRE_TRATAMIENTO, selectedTratamiento.getName());
        intent.putExtra(MainActivity.BUNDLE_TIEMPO_RESISTIR, selectedTratamiento.getMilisecondsResistivo());
        intent.putExtra(MainActivity.BUNDLE_TIEMPO_CAPACITIVO, selectedTratamiento.getMilisecondsCapacitivo());
        intent.putExtra(MainActivity.BUNDLE_ES_CAPACITIVO_PRIMERO, selectedTratamiento.getCapacitivoPrimero());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public RealmResults<TratamientoObject> getDataFromRealm() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TratamientoObject> result = null;
        RealmQuery<TratamientoObject> query = realm.where(TratamientoObject.class);
        // Execute the query:
        result = query.findAll();
        return result;
    }


    private void setUpRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        RealmResults<TratamientoObject> tratamientosObjectRealmResults = getDataFromRealm();

        tratamientoAdapter = new TratamientoAdapter(this, tratamientosObjectRealmResults, this, true);
        mRecyclerView.setAdapter(tratamientoAdapter);
    }

    @Override
    public void setTratamientoActual(TratamientoObject tratamientoActual) {
        selectedTratamiento = tratamientoActual;
        activityMenuDetalleTratamiento.setText("DETALLE DEL TRATAMIENTO: " + tratamientoActual.getName().toUpperCase());
        activityMenuTiempoResistivo.setText(formatSecondsinScreen(tratamientoActual.getMilisecondsResistivo()));
        activityMenuTiempoCapacitivo.setText(formatSecondsinScreen(tratamientoActual.getMilisecondsCapacitivo()));
        if(tratamientoActual.getCapacitivoPrimero()) {
            activityMenuTextResistivo.setText("CAPACITIVO");
            activityMenuTextCapacitivo.setText("RESISTIVO");
        } else {
            activityMenuTextResistivo.setText("RESISTIVO");
            activityMenuTextCapacitivo.setText("CAPACITIVO");
        }

        capacitivoActual = tratamientoActual.getMilisecondsCapacitivo();
        resistivoActual = tratamientoActual.getMilisecondsResistivo();
        esCapacitivoActual = tratamientoActual.getCapacitivoPrimero();
    }


    private String formatSecondsinScreen(long milliseconds) {
        Date date = new Date((long) (milliseconds));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedDate = simpleDateFormat.format(date);
        return formattedDate;
    }


    private void btnTiempoUpResistivo(){
        resistivoActual += 60 * 1000;
        activityMenuTiempoResistivo.setText(formatSecondsinScreen(resistivoActual));
    }

    private void btnTiempoDownResistivo(){
        if (resistivoActual<=60* 1000){
            resistivoActual = 0;
        } else {
            resistivoActual -= 60 * 1000;
        }

        activityMenuTiempoResistivo.setText(formatSecondsinScreen(resistivoActual));;
    }

    private void btnTiempoUpCapacitivo(){
        capacitivoActual += 60 * 1000;
        activityMenuTiempoCapacitivo.setText(formatSecondsinScreen(capacitivoActual));
    }

    private void btnTiempoDownCapacitivo(){
        if (capacitivoActual<=60* 1000){
            capacitivoActual = 0;
        } else {
            capacitivoActual -= 60 * 1000;
        }

        activityMenuTiempoCapacitivo.setText(formatSecondsinScreen(capacitivoActual));;
    }
}
