package com.education.lionel.bujatimes;

import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.annotation.Documented;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView txtResponse;

    ArrayList<News> newsArrayList;

    ListView listView;

    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsArrayList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listView);



        //initialize  txtResponse
        txtResponse = (TextView) findViewById(R.id.textViewResponseData);

        //Instantiate Requestqueue
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://akeza.net/";



        //  Request a string response from the url
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override

            public void onResponse(String response) {
                //Show the response
                txtResponse.setText(response);
                Toast.makeText(MainActivity.this, "We got data, Baby " , Toast.LENGTH_SHORT).show();

                //Parse HTML into a Document.
                Document doc = Jsoup.parse(response);
                Elements links = doc.select("div#ticker").select("a[href]");
                for (Element link : links) {

                    String linkHref = link.attr("href");
                    String linkTxt = link.text();

                    News news= new News();
                    news.title = linkTxt;
                    news.link= linkHref;

                    newsArrayList.add(news);

                }
               // Log.i("Item found ", links.attr("href"));

              arrayAdapter = new ArrayAdapter <>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, newsArrayList );
              listView.setAdapter(arrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Handle the error
                Toast.makeText(MainActivity.this, "Request Not Successful, Baby ", Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(this, "Request sent, Baby ", Toast.LENGTH_SHORT).show();
        //Add the request (stringRequest) to RequestQueue
        requestQueue.add(stringRequest);
    }


}
