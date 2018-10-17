package com.example.etudiant.videoannottool;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ArrayList<Video> arrayOfVideos = new ArrayList<Video>();







        final ArrayList<Annotation> arrayOfAnnotations1 = new ArrayList<Annotation>();
        ArrayList<Annotation> arrayOfAnnotations2 = new ArrayList<Annotation>();
        ArrayList<Annotation> arrayOfAnnotations3 = new ArrayList<Annotation>();
        ArrayList<Annotation> arrayOfAnnotationsEmpty = new ArrayList<Annotation>();

        Annotation annotation1 = new Annotation("annotation1");
        Annotation annotation2 = new Annotation("annotation2");
        Annotation annotation3 = new Annotation("annotation3");

        arrayOfAnnotations1.add(annotation1);
        arrayOfAnnotations1.add(annotation2);
        arrayOfAnnotations1.add(annotation3);

        arrayOfAnnotations2.add(annotation2);
        arrayOfAnnotations2.add(annotation1);
        arrayOfAnnotations2.add(annotation3);

        arrayOfAnnotations3.add(annotation3);
        arrayOfAnnotations3.add(annotation2);
        arrayOfAnnotations3.add(annotation1);

        Video video1= new Video("nom1","auteur1",arrayOfAnnotations1);
        arrayOfVideos.add(video1);

        Video video2= new Video("nom2","auteur2",arrayOfAnnotations2);
        arrayOfVideos.add(video2);
        Video video3= new Video("nom3","auteur3",arrayOfAnnotations3);
        arrayOfVideos.add(video3);





        final VideosAdapter videosAdapter = new VideosAdapter(this, arrayOfVideos);

        final AnnotationsAdapter annotationsAdapter= new AnnotationsAdapter(this,arrayOfAnnotationsEmpty);
        final AnnotationsAdapter annotationsAdapter2= new AnnotationsAdapter(this,arrayOfAnnotations1);

        final ListView listViewVideos = (ListView)  findViewById(R.id.lv_videos);
        listViewVideos.setAdapter(videosAdapter);





        final ListView listViewAnnotations = (ListView)  findViewById(R.id.lv_annotations);
        listViewAnnotations.setAdapter(annotationsAdapter);

        //Spinner catégorie
        ArrayList<String> spinnerList = new ArrayList<String>();

        spinnerList.add("item1");
        spinnerList.add("item2");

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);

        //Spinner sous-catégorie
        ArrayList<String> spinnerList2 = new ArrayList<String>();
        spinnerList2.add("item1");
        spinnerList2.add("item2");
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList2);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(spinnerAdapter2);




        listViewVideos.setClickable(true);

        listViewVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Video video = (Video) listViewVideos.getItemAtPosition(position);

                final AnnotationsAdapter annotationsAdapter2= new AnnotationsAdapter(listViewVideos.getContext(),video.getAnnotations());

                listViewAnnotations.setAdapter(annotationsAdapter2);


            }
        });

    }

}
