package fapp.dhbk.nhdtam.labproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CurrencyRate extends AppCompatActivity {

    TextView vlUSD, vlEUR, vlJPY, vlGBP, vlCHF, vlCAD, vlAUD, vlCNY;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_rate);

        vlUSD = (TextView) findViewById(R.id.valueUSD);
        vlEUR = (TextView) findViewById(R.id.valueEUR);
        vlJPY = (TextView) findViewById(R.id.valueJPY);
        vlGBP = (TextView) findViewById(R.id.valueGBP);
        vlCHF = (TextView) findViewById(R.id.valueCHF);
        vlCAD = (TextView) findViewById(R.id.valueCAD);
        vlAUD = (TextView) findViewById(R.id.valueAUD);
        vlCNY = (TextView) findViewById(R.id.valueCNY);

        btnBack = (Button) findViewById(R.id.btnBack);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://www.apilayer.net/api/live?access_key=8571f5257c3dc6eb3a551755d62c1b51&format=1");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CurrencyRate.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }



    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String str  = getXMLFromUrl(params[0]);
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject root = new JSONObject(s).getJSONObject("quotes");
                float vnd = Float.parseFloat(root.getString("USDVND"));
                vlUSD.setText(String.valueOf(vnd));
                vlEUR.setText(exchange(Float.parseFloat(root.getString("USDEUR")), vnd));
                vlJPY.setText(exchange(Float.parseFloat(root.getString("USDJPY")), vnd));
                vlCHF.setText(exchange(Float.parseFloat(root.getString("USDCHF")), vnd));
                vlGBP.setText(exchange(Float.parseFloat(root.getString("USDGBP")), vnd));
                vlCAD.setText(exchange(Float.parseFloat(root.getString("USDCAD")), vnd));
                vlAUD.setText(exchange(Float.parseFloat(root.getString("USDAUD")), vnd));
                vlCNY.setText(exchange(Float.parseFloat(root.getString("USDCNY")), vnd));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private String exchange(float cur1, float cur2){
        return String.valueOf(1 / cur1 * cur2);
    }

    private static String getXMLFromUrl(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
