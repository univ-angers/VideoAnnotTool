package com.master.info_ua.videoannottool.annotation;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.master.info_ua.videoannottool.Ecouteur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ControlerAnnotation implements Runnable {
    private Context _mainActivity;
    private Ecouteur m_ecouteur = null;
    private ArrayList<InfoAnno> listInfoAnno = new ArrayList<InfoAnno>();
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
        System.out.print(listAnno.get(0).getAnnotationStartTime());
        //ttt de videoAnnotation
        int i = 0;
        for (Annotation elt : listAnno) {
            //System.out.println(elt.getAnnotationStartTime())+"****f*sef*se*f*sef*sef*se*fs*ef*sef**************************************************************************************************************************************************************************************************************************************************************************************************************");
            InfoAnno tmp = new InfoAnno(arronditSeconde(elt.getAnnotationStartTime()), i, true);
            listInfoAnno.add(tmp);
            long fin = arronditSeconde(elt.getAnnotationStartTime() + elt.getAnnotationDuration());
            tmp = new InfoAnno(fin, i, false);
            listInfoAnno.add(tmp);
            i++;
        }
        Collections.sort(listInfoAnno);
        System.out.println(listInfoAnno.get(0).getTime()+"*****************************************************************************************************************************************************");

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
            while (last_pos < listInfoAnno.size()) {
                System.out.println("\n\n\n\n"+arronditSeconde(m_ecouteur.getVideoTime())+"\n\n" + listInfoAnno.get(last_pos).getTime()+"\n\n\n\n" );
                if (arronditSeconde(m_ecouteur.getVideoTime()) == listInfoAnno.get(last_pos).getTime()) {
                    if (listInfoAnno.get(last_pos).isDebut()) {
                        launch(listAnno.get(listInfoAnno.get(last_pos).getIndex()));
                        annotActive.add(listInfoAnno.get(last_pos).getIndex());
                    } else {
                        stop(listAnno.get(listInfoAnno.get(last_pos).getIndex()));
                        annotActive.remove(annotActive.lastIndexOf(listInfoAnno.get(last_pos).getIndex()));
                    }
                }
                last_pos++;
            }
            last_pos = 0;
            return true;
        } else {
            return false;
        }
    }

    public long arronditSeconde(long time) {
        return (time / 1000) * 1000;
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
        cancelled = false;
        while (checkTime() && (!cancelled)) {
            synchronized (this) {
                try {
                    System.out.println("coucou j'ai exécuter un tour de boucle");

                    this.wait(1000);
                    System.out.println("coucou j'ai exécuter un tour de boucle");
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