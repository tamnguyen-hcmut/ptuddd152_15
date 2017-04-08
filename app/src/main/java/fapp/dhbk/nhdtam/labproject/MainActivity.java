package fapp.dhbk.nhdtam.labproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private static final String ACCESS_KEY = "?access_key=8571f5257c3dc6eb3a551755d62c1b51";
    private static final String BASE_URL = "http://www.apilayer.net/api/";
    private static final String ENDPOINT_COURRENCIES = "list";
    private static final String ENDPOINT_CURRENCY_RATE = "live";

    Button btnShow, btnConvert;
    TextView txtResult;
    EditText edtAmount;
    Spinner spn1, spn2;
    float rateCur1, rateCur2;
    public String[] currArc = {"AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD",
    "BIF", "BML", "BND", "BOB", "BRL", "BSD", "BTC", "BTN", "BWP" ,"BYN", "BYR", "BZD", "CAD", "CDF", "CHF", "CLF", "CLP", "CNY",
    "COP", "CRC", "CUC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EEK", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "GBP",
    "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG", "HUF", "IDR", "TLS", "IMP", "INR", "IQD",
    "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR",
    "LRD" ,"LSL", "LVL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRO", "MUR", "MVR", "MWK", "MXN", "MYR", "MZN",
    "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB",
    "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD", "STD", "SVC", "SYP", "SZL", "THB", "TJS", "TMT",
    "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VEF" ,"VND", "VUV", "WST", "XAF", "XAG",
    "XAU", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMK", "ZMW", "ZWL"};
    final ArrayList<Currency> currencyArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(),"Welcome!!!",Toast.LENGTH_LONG).show();
        btnShow = (Button) findViewById(R.id.btnShow);
        btnConvert = (Button) findViewById(R.id.btnConvert);
        edtAmount = (EditText) findViewById(R.id.edtAmount);
        txtResult = (TextView) findViewById(R.id.txtResult);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadXML().execute("https://www.currency-iso.org/dam/downloads/lists/list_one.xml");
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSONList().execute(BASE_URL + ENDPOINT_COURRENCIES + ACCESS_KEY);
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSONLive().execute(BASE_URL + ENDPOINT_CURRENCY_RATE + ACCESS_KEY);
            }
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtAmount.getText()))
                    Toast.makeText(MainActivity.this, "Please enter the amount want to convert!", Toast.LENGTH_LONG);
                else{
                    int num = Integer.parseInt(edtAmount.getText().toString());
                    float result = num / rateCur1 * rateCur2;
                    txtResult.setText(String.valueOf(result));
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CurrencyRate.class);
                startActivity(intent);
            }
        });

    }

    class ReadJSONList extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            String str = getXMLFromUrl(params[0]);
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject root = new JSONObject(s).getJSONObject("currencies");
                for(int i = 0; i<currencyArrayList.size(); i++){
                    currencyArrayList.get(i).FullName = root.getString(currencyArrayList.get(i).AcronymName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            spn1 = (Spinner) findViewById(R.id.spnCurrency1);
            spn2 = (Spinner) findViewById(R.id.spnCurrency2);

            CurrencyAdapter currencyAdapter = new CurrencyAdapter(MainActivity.this, R.layout.currency_spinner, currencyArrayList);
            spn1.setAdapter(currencyAdapter);
            spn2.setAdapter(currencyAdapter);
        }
    }

    class ReadJSONLive extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            String str = getXMLFromUrl(params[0]);
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                final JSONObject root = new JSONObject(s).getJSONObject("quotes");
                rateCur1 = Float.parseFloat(root.getString("USD" + currencyArrayList.get(0).AcronymName));
                rateCur2 = Float.parseFloat(root.getString("USD" + currencyArrayList.get(0).AcronymName));
                spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(MainActivity.this, "Currency 1 is " + currencyArrayList.get(position).FullName, Toast.LENGTH_SHORT).show();
                        try {
                            rateCur1 = Float.parseFloat(root.getString("USD" + currencyArrayList.get(position).AcronymName));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(MainActivity.this, "Currency 2 is " + currencyArrayList.get(position).FullName, Toast.LENGTH_SHORT).show();
                        try {
                            rateCur2 = Float.parseFloat(root.getString("USD" + currencyArrayList.get(position).AcronymName));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class ReadXML extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String str = getXMLFromUrl(params[0]);
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            XMLDOMParser parser = new XMLDOMParser();
            Document doc = parser.getDocument(s);
            NodeList nodeList = doc.getElementsByTagName("CcyNtry");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                if (!parser.getValue(e, "CcyNm").equals("No universal currency")) {
                    Currency curr = new Currency(parser.getValue(e, "Ccy"), parser.getValue(e, "CcyNm"), parser.getValue(e, "CtryNm"), Integer.parseInt(parser.getValue(e, "CcyNbr")), parser.getValue(e, "CtryNm"));
                    currencyArrayList.add(curr);
                }
            }
            Collections.sort(currencyArrayList, new Comparator<Currency>() {
                @Override
                public int compare(Currency o1, Currency o2) {
                    return o1.AcronymName.compareTo(o2.AcronymName);
                }
            });
            boolean isHas = false;
            String temp = "";
            for(int i = 0; i<currencyArrayList.size(); i++){
                Currency cur = currencyArrayList.get(i);
                while (cur.AcronymName.equals(temp)){
                    currencyArrayList.remove(cur);
                    cur = currencyArrayList.get(i);
                }
                for(int j = 0; j<currArc.length; j++){
                    if(cur.AcronymName.equals(currArc[j])){
                        isHas = true;
                        break;
                    }
                    else isHas = false;
                }
                if(!isHas) {
                    currencyArrayList.remove(cur);
                    i--;
                }
                temp = cur.AcronymName;
            }


        }
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
