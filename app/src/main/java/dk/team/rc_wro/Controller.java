package dk.team.rc_wro;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.widget.VideoView;


import com.airbnb.lottie.LottieAnimationView;

public class Controller extends AppCompatActivity {

    private String serverIP;
    private int serverPort;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private Handler autoForwardHandler;
    private Runnable autoForwardRunnable;
    private boolean isAutoForwardEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        // Get IP and port from the intent
        Intent intent = getIntent();
        serverIP = intent.getStringExtra("SERVER_IP");
        serverPort = intent.getIntExtra("SERVER_PORT", -1);
        try {
            serverAddress = InetAddress.getByName(serverIP);
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing UDP connection", Toast.LENGTH_LONG).show();
        }

        setupButtons();
        setupAutoSwitch();
        setupClearButton();

        // Initialize and play video
        VideoView videoView = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.phone1); // Replace R.raw.your_video with your video file
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(mediaPlayer -> videoView.start());

        // Hide video after 3 seconds
        new Handler().postDelayed(() -> videoView.setVisibility(View.GONE), 3000);
        Toast.makeText(this,"Rotate the phone",Toast.LENGTH_LONG).show();
    }


    private void setupButtons() {
        ImageButton upwardButton = findViewById(R.id.upward);
        ImageButton rightwardButton = findViewById(R.id.rightward);
        ImageButton leftwardButton = findViewById(R.id.leftward);
        ImageButton backwardButton = findViewById(R.id.backward);
        ImageButton stopButton = findViewById(R.id.center);
        EditText dcmotor = findViewById(R.id.dcmotor);
        EditText centerservo = findViewById(R.id.center_servo);
        EditText leftservo = findViewById(R.id.left_servo);
        EditText rightservo = findViewById(R.id.right_servo);

        upwardButton.setOnClickListener(v -> {
            String dcMotorValue = dcmotor.getText().toString().trim();
            String centerServoValue = centerservo.getText().toString().trim();
            if (!dcMotorValue.isEmpty() && !centerServoValue.isEmpty()) {
                sendUDPMessage(dcMotorValue + "," + centerServoValue);
                addSentValue("forward  ↑");
            } else {
                Toast.makeText(this, "Please enter values for DC Motor and Center Servo", Toast.LENGTH_SHORT).show();
            }
        });

        rightwardButton.setOnClickListener(v -> {
            String dcMotorValue = dcmotor.getText().toString().trim();
            String rightServoValue = rightservo.getText().toString().trim();
            if (!dcMotorValue.isEmpty() && !rightServoValue.isEmpty()) {
                sendUDPMessage(dcMotorValue + "," + rightServoValue);
                addSentValue("right  →");
            } else {
                Toast.makeText(this, "Please enter values for DC Motor and Right Servo", Toast.LENGTH_SHORT).show();
            }
        });

        leftwardButton.setOnClickListener(v -> {
            String dcMotorValue = dcmotor.getText().toString().trim();
            String leftServoValue = leftservo.getText().toString().trim();
            if (!dcMotorValue.isEmpty() && !leftServoValue.isEmpty()) {
                sendUDPMessage(dcMotorValue + "," + leftServoValue);
                addSentValue("left  ←");
            } else {
                Toast.makeText(this, "Please enter values for DC Motor and Left Servo", Toast.LENGTH_SHORT).show();
            }
        });

        backwardButton.setOnClickListener(v -> {
            sendUDPMessage("0,0");
            addSentValue("backward  ↓");
        });
        stopButton.setOnClickListener(v -> {
            sendUDPMessage("stop");
            addSentValue("stop \uD83D\uDEAB");
        });

    }

    private void setupAutoSwitch() {
        Switch autoSwitch = findViewById(R.id.switch1);
        EditText dcmotor = findViewById(R.id.dcmotor);
        EditText centerservo = findViewById(R.id.center_servo);

        autoForwardHandler = new Handler();
        autoForwardRunnable = new Runnable() {
            @Override
            public void run() {
                if (isAutoForwardEnabled) {
                    String dcMotorValue = dcmotor.getText().toString().trim();
                    String centerServoValue = centerservo.getText().toString().trim();
                    if (!dcMotorValue.isEmpty() && !centerServoValue.isEmpty()) {
                        sendUDPMessage(dcMotorValue + "," + centerServoValue);
                        addSentValue("Auto-forward  ↑");
                    }
                    autoForwardHandler.postDelayed(this, 500); // Adjust the delay as needed
                }
            }
        };

        autoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String dcMotorValue = dcmotor.getText().toString().trim();
            String centerServoValue = centerservo.getText().toString().trim();

            if (isChecked) {
                if (dcMotorValue.isEmpty() || centerServoValue.isEmpty()) {
                    Toast.makeText(Controller.this, "Fill the values", Toast.LENGTH_SHORT).show();
                    autoSwitch.setChecked(false);
                } else {
                    isAutoForwardEnabled = true;
                    autoForwardHandler.post(autoForwardRunnable);
                }
            } else {
                isAutoForwardEnabled = false;
                autoForwardHandler.removeCallbacks(autoForwardRunnable);
            }
        });
    }

    private void setupClearButton() {
        ImageButton clearButton = findViewById(R.id.clear);
        clearButton.setOnClickListener(v -> {
            LinearLayout sentValuesLayout = findViewById(R.id.sentValuesLayout);
            if (sentValuesLayout.getChildCount() == 0) {
                Toast.makeText(Controller.this, "Nothing to Clear", Toast.LENGTH_SHORT).show();
            } else {
                sentValuesLayout.removeAllViews();
            }
        });
    }


    private void sendUDPMessage(String message) {
        new Thread(() -> {
            try {
                byte[] data = message.getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);
                socket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
    public void addSentValue(String value) {
        LinearLayout sentValuesLayout = findViewById(R.id.sentValuesLayout);
        TextView textView = new TextView(this);
        textView.setText(value);
        textView.setTextSize(16);
        textView.setPadding(0, 8, 0, 8);
        sentValuesLayout.addView(textView);

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

}
