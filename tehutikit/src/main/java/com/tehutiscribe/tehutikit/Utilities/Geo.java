package com.tehutiscribe.tehutikit.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Geo {
        public interface AddressFinderCallback {
            void onAddressFound(Address result);
        }

        public static void findCoordinatesViaAddress(Context context, final String address, final AddressFinderCallback callback) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(address, 10);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (addresses != null && addresses.size() > 1) {

                final List<Address> finalAddresses = addresses;

                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Select Address")
                        .setAdapter(
                                new ArrayAdapter<Address>(context, android.R.layout.simple_list_item_1, addresses) {
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View view = super.getView(position, convertView, parent);
                                        Address a = getItem(position);

                                        ((TextView) view).setText(String.format("%s %s %s",
                                                a.getAddressLine(0), a.getAddressLine(1), a.getAddressLine(2)));

                                        return view;
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        callback.onAddressFound(finalAddresses.get(i));
                                    }
                                }).create().show();

            } else if (addresses != null && addresses.size() > 0) {
                callback.onAddressFound(addresses.get(0));
            } else {
                Toast.makeText(context, "No Results Found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
