package com.example.asis.thoughtworksproj;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asis.thoughtworksproj.customadapters.CustomBeerAdapter;
import com.example.asis.thoughtworksproj.model.BeerDatas;
import com.example.asis.thoughtworksproj.networkconnections.GetBeerDataServices;
import com.example.asis.thoughtworksproj.networkconnections.MyRetrofitInstance;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.barbuzz.beerprogressview.BeerProgressView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CustomBeerAdapter.OnUpdateOfCart {

    private static final String SHARED_PREFERENCE_KEY = "beercraftsharedpreferenceashish007";

    private BeerProgressView beerProgressView;
    private AsyncTask<Boolean, Integer, Boolean> mProgress;
    private RecyclerView myRecyclerView;
    private CustomBeerAdapter myBeerAdapter;
    private CardView noInternentErr;
    private AutoCompleteTextView edt_search;
    private List<BeerDatas> beerDatasListCopy;
    private TextView btnFilterBeer, btnSortBeer, txtCartCount;
    private ArrayList<String> selectedBeerStyleList = new ArrayList<String>();
    private boolean frstTmeFilter = true, frstTimesSort = true, sortIsAscending = true;
     boolean[] filterCheckedList = new boolean[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        //Initializing UI
        initUI();

    }

    public void initUI(){
        beerProgressView = (BeerProgressView) findViewById(R.id.beerProgressView);
        beerProgressView.setBubbleCount(30);
        beerProgressView.setMax(100);
        beerProgressView.setAmplitude(6);
        beerProgressView.setBeerProgress(10);
        beerProgressView.setBeerColor(ContextCompat.getColor(this, R.color.lager));
        beerProgressView.setBubbleColor(ContextCompat.getColor(this, R.color.lager_bubble));

        txtCartCount = (TextView) findViewById(R.id.txt_cart_count);
        Log.d("asisi", ""+MyUtils.getNoOfIncart(this));
        txtCartCount.setText(MyUtils.getNoOfIncart(this).toString());

        edt_search = (AutoCompleteTextView) findViewById(R.id.edt_search_beer);
        edt_search.setThreshold(1);

        btnFilterBeer = (TextView) findViewById(R.id.filter_beer);
        btnSortBeer = (TextView) findViewById(R.id.sort_beer);

        final CardView carddHead = (CardView) findViewById(R.id.cardview_head);
        MyUtils.startAnimate(carddHead,R.anim.dateanimate,this);

        callService();

        noInternentErr = (CardView) findViewById(R.id.no_internet_err);

        btnSortBeer.setOnClickListener(this);
        btnFilterBeer.setOnClickListener(this);
        noInternentErr.setOnClickListener(this);
    }

    public void callService(){
        pourBeer();
        GetBeerDataServices myDataServices = MyRetrofitInstance.getRetrofitInstance().create(GetBeerDataServices.class);

        Call<List<BeerDatas>> beerDataCall = myDataServices.getAllBeerDatas();
        beerDataCall.enqueue(new Callback<List<BeerDatas>>() {
            @Override
            public void onResponse(Call<List<BeerDatas>> call, Response<List<BeerDatas>> response) {
                beerProgressView.setVisibility(View.GONE);
               beerDatasListCopy = response.body();
                setUpAutoCompleteTextView(beerDatasListCopy);
                generateCustomData(beerDatasListCopy);
                //Toast.makeText(MainActivity.this, "success-"+response.body().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<BeerDatas>> call, Throwable t) {
                //txt.setText("fail-"+t.getMessage());
                noInternentErr.setVisibility(View.VISIBLE);
                //MyUtils.startAnimate(noInternentErr, R.anim.mutiplezoominout, MainActivity.this);
            }
        });
    }

    public void generateCustomData(List<BeerDatas> beerDatasList){
        myRecyclerView = findViewById(R.id.customRecyclerView);
        myBeerAdapter = new CustomBeerAdapter(this,beerDatasList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        myRecyclerView.setLayoutManager(layoutManager);
        myRecyclerView.setAdapter(myBeerAdapter);
    }

    //Setup the autocomplete textview everytime things are filtered
    public void setUpAutoCompleteTextView(final List<BeerDatas> beerDataList){
        final ArrayList<String> beernameLists = new ArrayList<String>();
        for(BeerDatas currntbeer : beerDataList){
            beernameLists.add(currntbeer.getmName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_item, beernameLists);
        edt_search.setAdapter(adapter);
        edt_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String)adapterView.getItemAtPosition(position);
                int selectedBeerPos = beernameLists.indexOf(selection);
                ArrayList<BeerDatas> filterList = new ArrayList<BeerDatas>();
                filterList.add(beerDataList.get(selectedBeerPos));
                generateCustomData(filterList);
                MyUtils.hideKeyboard(MainActivity.this, edt_search);
            }
        });

        //refreshes all the list
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0){
                    generateCustomData(beerDatasListCopy);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edt_search.getRight() - edt_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        edt_search.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void pourBeer() {
        if (mProgress != null) {
            mProgress.cancel(true);
            mProgress = null;
            beerProgressView.setBeerProgress(0);
        }
        mProgress = new StartLoadingData(this, beerProgressView).execute(true);
    }

    public void openFilterDialog(){
        final ArrayList<String> beerStyleList = new ArrayList<String>();

        for(BeerDatas beerTempDta: beerDatasListCopy){
            beerStyleList.add(beerTempDta.getmBeerStyle());
        }

        Set<String> hs = new HashSet<>();
        hs.addAll(beerStyleList);
        beerStyleList.clear();
        beerStyleList.addAll(hs);
        beerStyleList.remove("");
        beerStyleList.remove(null);

        final String[] beerStyleArray = new String[beerStyleList.size()];

        int count = 0;
        if(frstTmeFilter) {
            filterCheckedList = new boolean[beerStyleList.size()];
            for (String x : beerStyleList) {
                beerStyleArray[count] = x;
                filterCheckedList[count] = false;
                count++;
            }
        }
        else{
            for (String x : beerStyleList) {
                beerStyleArray[count] = x;
                count++;
            }
        }

        frstTmeFilter = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMultiChoiceItems(beerStyleArray, filterCheckedList, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos, boolean isChecked) {
                filterCheckedList[pos] = isChecked;
                if(isChecked){
                    selectedBeerStyleList.add(beerStyleArray[pos]);
                }
                else if(selectedBeerStyleList.contains(beerStyleArray[pos])){
                    selectedBeerStyleList.remove(beerStyleArray[pos]);
                }
            }
        });

        builder.setIcon(R.mipmap.filter);
        builder.setTitle("Filter By Beer Style");
        builder.setCancelable(false);

        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("asisi",""+selectedBeerStyleList.size());
                ArrayList<BeerDatas> tempFIlteredDatas = new ArrayList<>();
                if(selectedBeerStyleList.size()!=0) {
                    for (BeerDatas beerStyle : beerDatasListCopy) {
                        if (selectedBeerStyleList.contains(beerStyle.getmBeerStyle()))
                            tempFIlteredDatas.add(beerStyle);
                        // Log.d("asisi beerdata",beerStyle.getmBeerStyle()+"--"+selectedBeerStyleList.contains(beerStyle.getmBeerStyle()));
                    }
                    //Toast.makeText(MainActivity.this, ""+tempFIlteredDatas.size(), Toast.LENGTH_SHORT).show();
                    generateCustomData(tempFIlteredDatas);
                    setUpAutoCompleteTextView(tempFIlteredDatas);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNeutralButton("Show All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedBeerStyleList.clear();
                for(int x=0; x<filterCheckedList.length;x++){
                    filterCheckedList[x] = false;
                }
                generateCustomData(beerDatasListCopy);
                setUpAutoCompleteTextView(beerDatasListCopy);
            }
        });

        builder.show();
    }

    public class StartLoadingData extends AsyncTask<Boolean, Integer, Boolean> {

        private static final int SLEEP_TIME = 70;
        private final BeerProgressView mBeerProgressView;
        private final Context mContext;

        public StartLoadingData(Context ctx, BeerProgressView beerProgressView) {
            mBeerProgressView = beerProgressView;
            mContext = ctx;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mBeerProgressView.setBeerProgress(values[0]);
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            for (int i = 0; i < 90; i++) {
                publishProgress(i);
                try {
                    if (i > 10) Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {

                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    //SORT BY ALCOHOL CONTENT
    public void sortByAlcoholContent(){
        ArrayList<Double> alcoholContLists = new ArrayList<>();
        ArrayList<BeerDatas> tempFilterBeerDatas = new ArrayList<>();
        for(BeerDatas x: beerDatasListCopy){
            alcoholContLists.add(x.getmAlcoholContInDouble());
        }
        HashSet<Double> hs = new HashSet<>();
        hs.addAll(alcoholContLists);
        alcoholContLists.clear();
        alcoholContLists.addAll(hs);

        if(frstTimesSort){
            frstTimesSort = false;
            Collections.sort(alcoholContLists);
            sortIsAscending = false;
            btnSortBeer.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.sort,0,R.mipmap.ascending,0);
        }
        else {
            if (sortIsAscending) { //Ascending order
                btnSortBeer.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.sort,0,R.mipmap.ascending,0);
                sortIsAscending = false;
                Collections.sort(alcoholContLists);
            } else { //Descending order
                btnSortBeer.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.sort,0,R.mipmap.descending,0);
                sortIsAscending = true;
                Collections.sort(alcoholContLists, Collections.<Double>reverseOrder());
            }
        }

        for(Double x:alcoholContLists){
            String alcoholCOntInString = String.valueOf(x);
            if(!alcoholCOntInString.equalsIgnoreCase("0.0")) {//For no data values. Beacuse, for blank values, will be added at the last
                for (BeerDatas filterBeerData : beerDatasListCopy) {
                    if(filterBeerData.getmAlcoholCont().equalsIgnoreCase(alcoholCOntInString))
                        tempFilterBeerDatas.add(filterBeerData);
                }
            }
            Log.d("asisi",""+x+"------"+(String.valueOf(x).equalsIgnoreCase(beerDatasListCopy.get(20).getmAlcoholCont())));
        }
        for(BeerDatas filterBeerDataCOpy : beerDatasListCopy){
            if(filterBeerDataCOpy.getmAlcoholCont().equalsIgnoreCase("0.0")){
                tempFilterBeerDatas.add(filterBeerDataCOpy);
            }
        }

        generateCustomData(tempFilterBeerDatas);
        setUpAutoCompleteTextView(tempFilterBeerDatas);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.filter_beer:{
                openFilterDialog();
                break;
            }

            case R.id.sort_beer:{
                sortByAlcoholContent();
                break;
            }

            case R.id.no_internet_err:{
                beerProgressView.setVisibility(View.VISIBLE);
                callService();
                noInternentErr.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    public void refreshCartCount() {
        txtCartCount.setText(MyUtils.getNoOfIncart(this).toString());
    }
}
