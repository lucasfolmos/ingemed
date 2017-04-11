package com.olmos.ingemed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.olmos.ingemed.aplication.BaseApplication;
import com.olmos.ingemed.utils.BusProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TRATAMIENTO = 1232;

    public static final String BUNDLE_TIEMPO_CAPACITIVO = "com.olmos.ingemed.BUNDLE_TIEMPO_CAPACITIVO";
    public static final String BUNDLE_TIEMPO_RESISTIR = "com.olmos.ingemed.BUNDLE_TIEMPO_RESISTIR";
    public static final String BUNDLE_NOMBRE_TRATAMIENTO = "com.olmos.ingemed.BUNDLE_NOMBRE_TRATAMIENTO";
    public static final int TRATAMIENT_GENERAL = 0;
    public static final int TRATAMIENT_CAPACITIVO = 1;
    public static final int TRATAMIENT_RESISTIVO = 2;
    public static final int DELAY = 100;
    public static final int PERIOD = 100;

    String mNombreTratamiento;
    long mTiempoCapacitivo;
    long mTiempoResistivo;

    boolean isTratamientoEnCurso = false;
    boolean isPlayActive = false;
    private int tipoTratamiento = TRATAMIENT_GENERAL;


    int potenciaActual = 50;
    int seguntosActuales = 0;


    @BindView(R.id.main_potencia_usr)
    TextView mainPotenciaUsr;
    @BindView(R.id.main_btn_potencia_up)
    ImageView mainBtnPotenciaUp;
    @BindView(R.id.main_btn_pontencia_down)
    ImageView mainBtnPontenciaDown;
    @BindView(R.id.main_time_usr)
    TextView mainTimeUsr;
    @BindView(R.id.main_btn_tiempo_up)
    ImageView mainBtnTiempoUp;
    @BindView(R.id.main_btn_tiempo_down)
    ImageView mainBtnTiempoDown;
    @BindView(R.id.main_resistivo_txt)
    TextView mainResistivoTxt;
    @BindView(R.id.main_resistivo_line)
    FrameLayout mainResistivoLine;
    @BindView(R.id.main_capacitivo_txt)
    TextView mainCapacitivoTxt;
    @BindView(R.id.main_capacitivo_line)
    FrameLayout mainCapacitivoLine;
    @BindView(R.id.main_tratamiento)
    TextView mainTratamiento;
    @BindView(R.id.main_play)
    ImageButton mainPlay;
    @BindView(R.id.main_stop)
    ImageButton mainStop;
    @BindView(R.id.main_modo_manual)
    Button mainModoManual;
    @BindView(R.id.main_menu)
    ImageButton mainMenu;
    @BindView(R.id.main_potencia_leida)
    TextView mainPotenciaLeida;
    @BindView(R.id.main_corriente_leida)
    TextView mainCorrienteLeida;
    @BindView(R.id.main_impedancia_leida)
    TextView mainImpedanciaLeida;
    @BindView(R.id.main_potencia_progress)
    ArcProgress mainPotenciaProgress;
    @BindView(R.id.main_time_progress)
    ArcProgress mainTimeProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeLayouts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.unregister(this);
    }

    public void initializeLayouts() {
        mainPotenciaUsr.setText("" + potenciaActual + "%");
        mainPotenciaProgress.setProgress(potenciaActual);

        formatSecondsinScreen();
        mainTimeProgress.setProgress(100);

        clickResistivo();

        mainTratamiento.setText("--");

        initializeLongClicks();

    }


    @OnClick({R.id.main_btn_potencia_up, R.id.main_btn_pontencia_down, R.id.main_btn_tiempo_up, R.id.main_btn_tiempo_down, R.id.main_resistivo_txt, R.id.main_capacitivo_txt, R.id.main_play, R.id.main_stop, R.id.main_modo_manual, R.id.main_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_potencia_up:
                btnPotenciaUp();
                break;
            case R.id.main_btn_pontencia_down:
                btnPotenciaDown();
                break;
            case R.id.main_btn_tiempo_up:
                btnTiempoUp();
                break;
            case R.id.main_btn_tiempo_down:
                btnTiempoDown();
                break;
            case R.id.main_resistivo_txt:
                clickResistivo();
                break;
            case R.id.main_capacitivo_txt:
                clickCapacitivo();
                break;
            case R.id.main_play:
                clickPlay();
                break;
            case R.id.main_stop:
                clickStop();
                break;
            case R.id.main_modo_manual:
                clickModoManual();
                break;
            case R.id.main_menu:
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TRATAMIENTO);
                break;
        }
    }

    private void clickModoManual() {
        //Enable buttons
        mainBtnPotenciaUp.setEnabled(true);
        mainBtnPontenciaDown.setEnabled(true);
        mainBtnTiempoDown.setEnabled(true);
        mainBtnTiempoUp.setEnabled(true);
        mainResistivoTxt.setEnabled(true);
        mainCapacitivoTxt.setEnabled(true);

        mNombreTratamiento = "";
        mainTratamiento.setText("--");

        mainCapacitivoTxt.setEnabled(true);
        mainResistivoTxt.setEnabled(true);
        clickResistivo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TRATAMIENTO && resultCode == Activity.RESULT_OK) {
            mNombreTratamiento = data.getStringExtra(BUNDLE_NOMBRE_TRATAMIENTO);
            mTiempoCapacitivo = data.getLongExtra(BUNDLE_TIEMPO_CAPACITIVO, 0);
            mTiempoResistivo = data.getLongExtra(BUNDLE_TIEMPO_RESISTIR, 0);

            mainTratamiento.setText(mNombreTratamiento);
            seguntosActuales = (int) ((mTiempoCapacitivo + mTiempoResistivo) / 1000);
            formatSecondsinScreen();

            //indicator resistivo y capacitivo
            disableCapacitivoAndResistivo();

            //Disable/Enable buttons
            mainBtnPotenciaUp.setEnabled(true);
            mainBtnPontenciaDown.setEnabled(true);
            mainBtnTiempoDown.setEnabled(false);
            mainBtnTiempoUp.setEnabled(false);
            mainResistivoTxt.setEnabled(false);
            mainCapacitivoTxt.setEnabled(false);
        }
    }

    private void clickPlay() {

        mainStop.setEnabled(true);
        if (!isPlayActive) {
            mainPlay.setImageResource(R.drawable.ic_pause_black_48dp);
            isPlayActive = true;

            if (!isTratamientoEnCurso) {
                if (mNombreTratamiento == null || mNombreTratamiento.isEmpty()) {
                    //MODO MANUAL
                    BaseApplication.getInstance().startModoManual(seguntosActuales * 1000, potenciaActual, tipoTratamiento == TRATAMIENT_RESISTIVO);
                } else {
                    BaseApplication.getInstance().startModoPrograma(mTiempoCapacitivo, mTiempoResistivo, potenciaActual);
                }
                isTratamientoEnCurso = true;
            } else {
                BaseApplication.getInstance().play();
            }

        } else {
            mainPlay.setImageResource(R.drawable.ic_play_arrow_black_48dp);
            isPlayActive = false;
        }
        mainModoManual.setEnabled(false);
        mainMenu.setEnabled(false);

        mainBtnPotenciaUp.setEnabled(false);
        mainBtnPontenciaDown.setEnabled(false);
        mainBtnTiempoUp.setEnabled(false);
        mainBtnTiempoDown.setEnabled(false);


    }

    private void clickStop() {
        mainStop.setEnabled(false);
        mainPlay.setEnabled(true);
        mainModoManual.setEnabled(true);
        mainMenu.setEnabled(true);

        mainBtnPotenciaUp.setEnabled(true);
        mainBtnPontenciaDown.setEnabled(true);
        if (mNombreTratamiento == null || mNombreTratamiento.isEmpty()) {
            mainBtnTiempoUp.setEnabled(true);
            mainBtnTiempoDown.setEnabled(true);
        } else {
            mainBtnTiempoUp.setEnabled(false);
            mainBtnTiempoDown.setEnabled(false);
        }

        mainPlay.setImageResource(R.drawable.ic_play_arrow_black_48dp);
        isPlayActive = false;
        isTratamientoEnCurso = false;
    }

    private void btnPotenciaUp() {
        if (potenciaActual >= 90) {
            potenciaActual = 100;
        } else {
            potenciaActual += 10;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainPotenciaUsr.setText("" + potenciaActual + "%");
                mainPotenciaProgress.setProgress(potenciaActual);
            }
        });
    }

    private void btnPotenciaDown() {
        if (potenciaActual <= 10) {
            potenciaActual = 0;
        } else {
            potenciaActual -= 10;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainPotenciaUsr.setText("" + potenciaActual + "%");
                mainPotenciaProgress.setProgress(potenciaActual);
            }
        });
    }

    private void btnTiempoUp() {
        seguntosActuales += 60;
        formatSecondsinScreen();
    }

    private void btnTiempoDown() {
        if (seguntosActuales <= 60) {
            seguntosActuales = 0;
        } else {
            seguntosActuales -= 60;
        }

        formatSecondsinScreen();
    }

    private void formatSecondsinScreen() {
        Date date = new Date((long) (seguntosActuales * 1000));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        final String formattedDate = simpleDateFormat.format(date);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainTimeUsr.setText(formattedDate);
            }
        });
    }

    public void clickResistivo() {
        mainResistivoTxt.setTextColor(getResources().getColor(R.color.white));
        mainResistivoLine.setVisibility(View.VISIBLE);
        mainCapacitivoTxt.setTextColor(getResources().getColor(R.color.optionNotSelected));
        mainCapacitivoLine.setVisibility(View.INVISIBLE);
        tipoTratamiento = TRATAMIENT_RESISTIVO;
    }

    public void clickCapacitivo() {
        mainResistivoTxt.setTextColor(getResources().getColor(R.color.optionNotSelected));
        mainResistivoLine.setVisibility(View.INVISIBLE);
        mainCapacitivoTxt.setTextColor(getResources().getColor(R.color.white));
        mainCapacitivoLine.setVisibility(View.VISIBLE);
        tipoTratamiento = TRATAMIENT_CAPACITIVO;
    }

    public void disableCapacitivoAndResistivo() {
        mainResistivoTxt.setTextColor(getResources().getColor(R.color.optionNotSelected));
        mainResistivoLine.setVisibility(View.INVISIBLE);
        mainCapacitivoTxt.setTextColor(getResources().getColor(R.color.optionNotSelected));
        mainCapacitivoLine.setVisibility(View.INVISIBLE);

        mainCapacitivoTxt.setEnabled(false);
        mainResistivoTxt.setEnabled(false);
    }

    private void initializeLongClicks() {
        mainBtnTiempoUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(mainBtnTiempoUp.isPressed()) {
                            btnTiempoUp();
                        }
                        else
                            timer.cancel();
                    }
                },DELAY,PERIOD);

                return true;
            }
        });

        mainBtnTiempoDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(mainBtnTiempoDown.isPressed()) {
                            btnTiempoDown();
                        }
                        else
                            timer.cancel();
                    }
                },DELAY,PERIOD);

                return true;
            }
        });

        mainBtnPotenciaUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(mainBtnPotenciaUp.isPressed()) {
                            btnPotenciaUp();
                        }
                        else
                            timer.cancel();
                    }
                },DELAY,PERIOD);

                return true;
            }
        });

        mainBtnPontenciaDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(mainBtnPontenciaDown.isPressed()) {
                            btnPotenciaDown();
                        }
                        else
                            timer.cancel();
                    }
                },DELAY,PERIOD);

                return true;
            }
        });
    }


}
