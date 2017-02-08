package nodomain.freeyourgadget.gadgetbridge.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import nodomain.freeyourgadget.gadgetbridge.GBApplication;
import nodomain.freeyourgadget.gadgetbridge.R;
import nodomain.freeyourgadget.gadgetbridge.adapter.GBAlarmListAdapter;
import nodomain.freeyourgadget.gadgetbridge.impl.GBAlarm;
import nodomain.freeyourgadget.gadgetbridge.impl.GBDevice;
import nodomain.freeyourgadget.gadgetbridge.util.Prefs;


public class ConfigureAlarms extends GBActivity {

    private static final int REQ_CONFIGURE_ALARM = 1;

    private GBAlarmListAdapter mGBAlarmListAdapter;
    private Set<String> preferencesAlarmListSet;
    private boolean avoidSendAlarmsToDevice;
    private GBDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configure_alarms);

        device = getIntent().getParcelableExtra(GBDevice.EXTRA_DEVICE);

        Prefs prefs = GBApplication.getPrefs();
    }

    @Override
    protected void onPause() {
        if (!avoidSendAlarmsToDevice) {
            sendAlarmsToDevice();
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CONFIGURE_ALARM) {
            avoidSendAlarmsToDevice = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // back button
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void configureAlarm(GBAlarm alarm) {
        avoidSendAlarmsToDevice = true;
        Intent startIntent = new Intent(getApplicationContext(), AlarmDetails.class);
        startIntent.putExtra("alarm", alarm);
        startIntent.putExtra(GBDevice.EXTRA_DEVICE, getDevice());
        startActivityForResult(startIntent, REQ_CONFIGURE_ALARM);
    }

    private GBDevice getDevice() {
        return device;
    }

    private void sendAlarmsToDevice() {
        GBApplication.deviceService().onSetAlarms(mGBAlarmListAdapter.getAlarmList());
    }
}
