package dk.team.rc_wro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

    public class MainActivity extends AppCompatActivity {

        private EditText etIP, etPort;
        private ImageButton connectButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            etIP = findViewById(R.id.et_ip_port);
            etPort = findViewById(R.id.et_topic);
            connectButton = findViewById(R.id.connect_server);

            connectButton.setOnClickListener(v -> onConnectButtonClick());
        }

        private void onConnectButtonClick() {
            try {
                String serverIP = etIP.getText().toString().trim();
                int serverPort = Integer.parseInt(etPort.getText().toString().trim());

                // Pass IP and port to the next activity
                Intent intent = new Intent(MainActivity.this, Controller.class);
                intent.putExtra("SERVER_IP", serverIP);
                intent.putExtra("SERVER_PORT", serverPort);
                startActivity(intent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(this, "Please enter a valid IP address and port number", Toast.LENGTH_SHORT).show();
            }
        }
    }