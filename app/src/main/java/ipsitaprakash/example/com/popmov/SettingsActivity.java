package ipsitaprakash.example.com.popmov;

import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by IpsitaPrakash on 15/06/15.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferencesToSummaryValue(findPreference("sort_criteria"));

    }

    private void bindPreferencesToSummaryValue(Preference preference)
    {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(),""));

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {

        String prefValue = (String) newValue;
        if(preference instanceof ListPreference)
        {
            ListPreference listPreference = (ListPreference) preference;
            int prefValIndex = ((ListPreference) preference).findIndexOfValue(prefValue);
            if(prefValIndex>=0)
            {
                preference.setSummary(listPreference.getEntries()[prefValIndex]);
            }
            else
            {
                preference.setSummary(prefValue);
            }
        }
        return true;
    }
}
