package com.master.info_ua.videoannottool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.master.info_ua.videoannottool.adapter.SpinnerAdapter;
import com.master.info_ua.videoannottool.dialog.DialogAddSubCategorie;
import com.master.info_ua.videoannottool.dialog.DialogEditCategorie;
import com.master.info_ua.videoannottool.dialog.DialogEditSubCategorie;
import com.master.info_ua.videoannottool.util.Categorie;
import com.master.info_ua.videoannottool.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.master.info_ua.videoannottool.MainActivity.READ_CATEGORY_CODE;

public class CategoryActivity extends Activity implements DialogEditCategorie.EditCategoryDialogListener, DialogAddSubCategorie.AddSubCategoryDialogListener, DialogEditSubCategorie.EditSubCategoryDialogListener{

    private ListView lv_category;
    private ListView lv_sub_category;
    private ArrayAdapter<Categorie> spinnerAdapter;
    private ArrayAdapter<Categorie> spinnerAdapter2;
    private EditText ed_cat_title;
    private Button btn_valider;

    //Les catégories + sous-catégories envoyées dans le mainActivity
    private ArrayList<Categorie> list_categorie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        lv_category=(ListView) findViewById(R.id.lv_category);
        lv_sub_category=(ListView) findViewById(R.id.lv_sub_category);
        ed_cat_title = (EditText) findViewById(R.id.ed_cat_title);
        btn_valider = (Button) findViewById(R.id.btnValiderCat);
        list_categorie=new ArrayList<>();

        btn_valider.setOnClickListener(btn_Listener);
        Intent intent = getIntent();
        List<Categorie> categorieList = new ArrayList<>();
        categorieList = intent.getParcelableArrayListExtra("categorieList");
        categorieList.remove(0);
        for(Categorie categorie : categorieList){
            list_categorie.add(categorie);
        }

        spinnerAdapter = new SpinnerAdapter(this, R.layout.item_category, categorieList){
            @Override
            public boolean isEnabled(int position) {
                return true;
            }
        };

        List<Categorie> subCategorieList = new ArrayList<>();
        spinnerAdapter2 = new SpinnerAdapter(this, R.layout.item_category, subCategorieList);

        lv_category.setAdapter(spinnerAdapter);
        lv_sub_category.setAdapter(spinnerAdapter2);

        lv_category.setOnItemClickListener(CategoryListener);
        registerForContextMenu(lv_category);
        registerForContextMenu(lv_sub_category);
    }

//    Ajout d'une catégorie après validation
    protected View.OnClickListener btn_Listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.out.println("                           ------- "+ed_cat_title.getText().toString());
            boolean isValid = true;
            for(Categorie cat : list_categorie){
                if(cat.getName().toUpperCase().equals(ed_cat_title.getText().toString().toUpperCase())) {
                    Toast.makeText(v.getContext(), "Cette catégorie existe déjà", Toast.LENGTH_LONG).show();
                    isValid = false;
                    break;
                }
            }
            // Vérifie que le dossier créer par l'utilisateur ne s'appelle pas "annotations" qu'importe la casse
            if (ed_cat_title.getText().toString().matches("[a,A][n,N][n,N][o,O][t,T][a,A][t,T][i,I][o,O][n,N][s,S]"+"\\s*")
                    || ed_cat_title.getText().toString().isEmpty()) {
                Toast.makeText(v.getContext(),"Nom de catégorie non autorisé",Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if(isValid) {
                list_categorie.add(new Categorie(ed_cat_title.getText().toString(), null, ed_cat_title.getText().toString()));
                spinnerAdapter.add(new Categorie(ed_cat_title.getText().toString(), null, ed_cat_title.getText().toString()));
            }
            ed_cat_title.setText("");
        }
    };

    protected AdapterView.OnItemClickListener CategoryListener= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Categorie c = (Categorie) parent.getItemAtPosition(position);
            c.setSubCategories(findCat(c.getName()).getSubCategories());
            List<Categorie> subCategorieList=new ArrayList<>();
            subCategorieList.addAll(c.getSubCategories());
            spinnerAdapter2 = new SpinnerAdapter(view.getContext(), R.layout.item_category, subCategorieList){
                @Override
                public boolean isEnabled(int position) {
                    return true;
                }
            };
            lv_sub_category.setAdapter(spinnerAdapter2);
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.context_menu_cat, menu);
        if (v.getId() == R.id.lv_category) {
            menu.findItem(R.id.edit_categorie).setVisible(true);
            menu.findItem(R.id.delete_categorie).setVisible(true);
            menu.findItem(R.id.add_sub_cat).setVisible(true);
            menu.findItem(R.id.edit_sub_cat).setVisible(false);
            menu.findItem(R.id.delete_sub_cat).setVisible(false);
        }
        if (v.getId() == R.id.lv_sub_category) {
			menu.findItem(R.id.edit_categorie).setVisible(false);
            menu.findItem(R.id.delete_categorie).setVisible(false);
            menu.findItem(R.id.add_sub_cat).setVisible(false);
            menu.findItem(R.id.edit_sub_cat).setVisible(true);
            menu.findItem(R.id.delete_sub_cat).setVisible(true);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Categorie categorie = spinnerAdapter.getItem(info.position);
        switch (item.getItemId()) {
            case R.id.edit_categorie:
                DialogEditCategorie editCategorie = new DialogEditCategorie(this, categorie);
                editCategorie.showDialogEdit();
                return true;
            case R.id.delete_categorie:
                categorie = spinnerAdapter.getItem(info.position);
                list_categorie.remove(categorie);
                spinnerAdapter.remove(categorie);
                File file = getExternalFilesDir(categorie.getName());
                Util.deleteRecursiveDirectory(file);
                return true;
            case R.id.add_sub_cat:
                DialogAddSubCategorie addSubCategorie = new DialogAddSubCategorie(this,categorie);
                addSubCategorie.showDialogEdit();
                return true;
            case R.id.edit_sub_cat:
                categorie = spinnerAdapter2.getItem(info.position);
                DialogEditSubCategorie editSubCategorie = new DialogEditSubCategorie(this, categorie);
                editSubCategorie.showDialogEdit();
                return true;
            case R.id.delete_sub_cat:
                categorie = spinnerAdapter2.getItem(info.position);
                for(Categorie cat: list_categorie){
                    System.out.println(cat.getName()+"    "+categorie.getParentName());
                    if (cat.getName().matches(categorie.getParentName()))
                    {
                        System.out.println("Removed "+cat.getName()+"    "+categorie.getParentName());
                        cat.removeSubCategorie(categorie);
                        File file2 = getExternalFilesDir(cat.getName() +File.separator+ categorie.getName());
                        Util.deleteRecursiveDirectory(file2);
                    }
                }
                spinnerAdapter2.remove(categorie);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onSaveEditCategorie(Categorie categorie, String title) {
        for (int i=0;i<list_categorie.size();i++)
        {
            if (list_categorie.get(i).getName().matches(categorie.getName()))
            {
                list_categorie.get(i).setName(title);
                //On change le parent également
                for (int j=0; j < list_categorie.get(i).getSubCategories().size();j++) {
                    list_categorie.get(i).getSubCategories().get(j).setParentName(title);
                    list_categorie.get(i).getSubCategories().get(j).setPath(title + "/" + list_categorie.get(i).getSubCategories().get(j).getName());
                    System.out.println("Chemin " + list_categorie.get(i).getSubCategories().get(j).getPath());
                }
            }
        }
        categorie.setName(title);
        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveAddSubCategory(Categorie categorie,String title) {
        findCat(categorie.getName()).getSubCategories().add(new Categorie(title, categorie.getName(),categorie.getPath()+"/"+title));
        spinnerAdapter2.add(new Categorie(title,categorie.getName(),categorie.getPath()+"/"+title));
        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveEditSubCategorie(Categorie categorie, String title) {
        Categorie item = new Categorie(title, categorie.getParentName(),categorie.getPath()+"/"+title);
        System.out.println(findCat(categorie.getParentName())+"     " + categorie.getParentName());
        for(int i = 0; i<findCat(categorie.getParentName()).getSubCategories().size();i++)
        {
            if (findCat(categorie.getParentName()).getSubCategories().get(i).getName().matches(categorie.getName())) {
                findCat(categorie.getParentName()).getSubCategories().get(i).setName(title);
            }
        }
        spinnerAdapter2.notifyDataSetChanged();

    }

    public Categorie findCat(String s){
        for(int i=0; i<list_categorie.size();i++){
            if (list_categorie.get(i).getName().matches(s)){
                return list_categorie.get(i);
            }
        }
        return null;
    }

    public Categorie findSubCat( List<Categorie> liste, String s){
        for(int i=0; i<liste.size();i++){
            if (liste.get(i).getName().matches(s)){
                return liste.get(i);
            }
        }
        return null;
    }

    public void CategoryResult(View view) {
        ArrayList<Categorie> tmpListCategorie = new ArrayList<Categorie>();
        tmpListCategorie.add(new Categorie("Catégorie", null, "/"));
        tmpListCategorie.addAll(list_categorie);
        boolean checkNoSubCat = true;
        for(Categorie categorie : list_categorie){
            //Provisoire... Créer une catégorie sans sous-catégories peut poser quelques problèmes pour le moment
            if(categorie.getSubCategories().isEmpty()) {
                Toast.makeText(view.getContext(), "La catégorie " + categorie.getName() + " ne possède aucune sous-catégorie", Toast.LENGTH_LONG).show();
                checkNoSubCat = false;
                break;
            }
        }
        if(checkNoSubCat) {
            Intent returnintent = new Intent();
            returnintent.putParcelableArrayListExtra("Categorie", tmpListCategorie);
            setResult(READ_CATEGORY_CODE, returnintent);
            finish();
        }
    }
}
