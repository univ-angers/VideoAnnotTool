package com.master.info_ua.videoannottool.annotation;

import android.content.Context;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.master.info_ua.videoannottool.MainActivity;
import com.master.info_ua.videoannottool.annotation.Annotation;
import com.master.info_ua.videoannottool.annotation.VideoAnnotation;
import com.master.info_ua.videoannottool.Ecouteur;
import com.master.info_ua.videoannottool.annotation.infoAnno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ControlerAnnotation implements Runnable {
    private Context _mainActivity;
    private Ecouteur m_ecouteur = null;
    private ArrayList<infoAnno> ListInfoAnno = new ArrayList<infoAnno>();
    private List<Annotation> listAnno;
    private int last_pos = 0;
    private ArrayList<Integer> annotActive = new ArrayList<>();
    private boolean cancelled = false;
    private Handler mainHandler;

    public void setLast_pos(int last_pos) {
        this.last_pos = last_pos;
    }

    public ControlerAnnotation(Context context, Ecouteur m_e, VideoAnnotation videoAnnotation, Handler _mainHandler) {
        mainHandler = _mainHandler;
        _mainActivity = context;
        m_ecouteur = m_e;
        listAnno = videoAnnotation.getAnnotationList();
        //ttt de videoAnnotation
        int i = 0;
        for (Annotation elt : listAnno) {
            infoAnno tmp = new infoAnno(arronditSeconde(elt.getAnnotationStartTime()), i, true);
            ListInfoAnno.add(tmp);
            long fin = arronditSeconde(elt.getAnnotationStartTime() + elt.getAnnotationDuration());
            tmp = new infoAnno(fin, i, false);
            ListInfoAnno.add(tmp);
        }
        Collections.sort(ListInfoAnno);

    }

    public void updateAnno(VideoAnnotation update) {
        listAnno = update.getAnnotationList();
    }

    // verifie si il existe des annotation qui n'ont pas été lancer depuis le dernier appel et positionne le curseur sur le prochain element a vérifier
    // stop aussi les annotation qui doive se terminer
    public boolean checkTime() {
        ;
        //verifier le temps et lancer launch avec le paramétre correspondant
        if (m_ecouteur.getPlayer().getDuration() >= m_ecouteur.getVideoTime()) {
            while ((last_pos < ListInfoAnno.size() && (arronditSeconde(m_ecouteur.getVideoTime()) <= ListInfoAnno.get(last_pos).getTime()))) {
                if (arronditSeconde(m_ecouteur.getVideoTime()) == ListInfoAnno.get(last_pos).getTime()) {
                    if (ListInfoAnno.get(last_pos).isDebut()) {
                        launch(listAnno.get(ListInfoAnno.get(last_pos).getIndex()));
                        annotActive.add(ListInfoAnno.get(last_pos).getIndex());
                    } else {
                        stop(listAnno.get(ListInfoAnno.get(last_pos).getIndex()));
                        annotActive.remove(ListInfoAnno.get(last_pos).getIndex());
                    }
                }
                last_pos++;
            }
            return true;
        } else {
            this.cancel();
            return false;
        }
    }

    public long arronditSeconde(long time) {
        return (time % 1000) * 1000;
    }

    public void launch(Annotation anno) {
        switch (anno.getAnnotationType()) {
            case DRAW:
                launchDraw();
                break;
            case TEXT:
                launchText();
                break;
            case ZOOM:
                launchZoom();
                break;
            case AUDIO:
                launchAudio();
                break;
            case SLOWMOTION:
                launchSlowmo(anno.getSlowMotionSpeed());
                break;
            default:
                System.out.println("Error de lancement");
                break;
        }
    }

    public void stop(Annotation anno) {
        switch (anno.getAnnotationType()) {
            case DRAW:
                stopDraw();
                break;
            case TEXT:
                stopText();
                break;
            case ZOOM:
                stopZoom();
                break;
            case AUDIO:
                stopAudio();
                break;
            case SLOWMOTION:
                stopSlowmo();
                break;
            default:
                System.out.println("Error de d'arret");
                break;
        }
    }

    public void launchDraw() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_mainActivity, "je lance l'annotaion dessin", Toast.LENGTH_LONG).show();
            }
        });
        //encore du travail
    }

    public void launchText() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_mainActivity, "je lance l'annotaion text", Toast.LENGTH_LONG).show();
            }
        });
        //encore du travail
    }

    public void launchZoom() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_mainActivity, "je lance l'annotaion zoom", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void launchAudio() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {


                Toast.makeText(_mainActivity, "je lance l'annotaion audio", Toast.LENGTH_LONG).show();
            }
        });
        //encore du travail
    }

    public void launchSlowmo(final float slow) {
        mainHandler.post(new Runnable() {
            float slowRun = slow;

            @Override
            public void run() {


                Toast.makeText(_mainActivity, "je lance l'annotaion ralenti", Toast.LENGTH_LONG).show();
                m_ecouteur.setSpeed(slowRun);
            }
        });
    }

    public void stopDraw() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_mainActivity, "je stop l'annotaion dessin", Toast.LENGTH_LONG).show();
                //encore du
            }
        });
    }

    public void stopText() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_mainActivity, "je stop l'annotaion text", Toast.LENGTH_LONG).show();
                //encore du travail
            }
        });
    }

    public void stopZoom() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_mainActivity, "je stop l'annotaion zoom", Toast.LENGTH_LONG).show();
                //encore du travail
            }
        });

    }

    public void stopAudio() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_mainActivity, "je stop l'annotaion audio", Toast.LENGTH_LONG).show();
                //encore du travail
            }
        });
    }

    public void stopSlowmo() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_mainActivity, "je stop l'annotaion ralenti", Toast.LENGTH_LONG).show();
                m_ecouteur.setSpeed(1f);
            }
        });
    }

    @Override
    public void run() {
        while (checkTime() && (!cancelled)) {
            synchronized (this) {
                try {
                    this.wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancel() {
        cancelled = true;
    }
}