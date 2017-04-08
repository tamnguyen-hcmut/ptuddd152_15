package fapp.dhbk.nhdtam.labproject;

/**
 * Created by Asus on 3/16/2017.
 */

public class Currency {
    public String AcronymName;
    public String FullName;
    public String CountryName;
    public Integer Code;
    public String Image;

    public Currency(String acronymName, String fullName, String countryName, Integer code, String image) {
        AcronymName = acronymName;
        FullName = fullName;
        CountryName = countryName;
        Code = code;
        Image = image;
    }

}
