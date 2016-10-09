package elena.notesapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Note extends MainActivity {

    EditText title,note;
    Button remove, save, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        title=(EditText)findViewById(R.id.titleText);
        note=(EditText)findViewById(R.id.noteText);
        remove=(Button)findViewById(R.id.removeButton);
        save=(Button)findViewById(R.id.saveButton);
        message=(Button)findViewById(R.id.smsButton);

      if(this.getIntent().hasExtra("title")&&this.getIntent().hasExtra("body")){
            title.setText(this.getIntent().getExtras().getString("title"));
            note.setText(this.getIntent().getExtras().getString("body"));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i=new Intent(v.getContext(),MainActivity.class);
                if(getIntent().hasExtra("title")&&getIntent().hasExtra("body")&&
                    title.getText().toString().isEmpty() == false && note.getText().toString().isEmpty() == false)
                { i.putExtra("modify",1);
                    i.putExtra("at",getIntent().getExtras().getString("title"));
                    Notes n = new Notes("", "");
                    n.setTitle(title.getText().toString());
                    n.setBody(note.getText().toString());

                    i.putExtra("KeyTitle", n.getTitle());
                    i.putExtra("KeyBody", n.getBody());

                   finish();
                    startActivity(i);}

                else{
                i.putExtra("modify", 0);

                    if (title.getText().toString().isEmpty() == false && note.getText().toString().isEmpty() == false) {
                        Notes n = new Notes("", "");
                        n.setTitle(title.getText().toString());
                        n.setBody(note.getText().toString());

                        i.putExtra("KeyTitle", n.getTitle());
                        i.putExtra("KeyBody", n.getBody());

                        Toast.makeText(v.getContext(), "Added", Toast.LENGTH_SHORT).show();

                       finish();
                        startActivity(i);
                    } else{Toast.makeText(v.getContext(), "Not added: field(s) not completed", Toast.LENGTH_SHORT).show();
                        i.putExtra("modify", 2);
                        i.putExtra("KeyTitle", title.getText().toString());
                        finish();
                        startActivity(i);
                    }
                }
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),MainActivity.class);
                i.putExtra("modify", 3);
                i.putExtra("KeyTitle", title.getText().toString());
                finish();
                startActivity(i);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));

                sendIntent.putExtra("sms_body",title.getText().toString()+"\n"+note.getText().toString());
                if(isIntentAvailable(sendIntent))
                         startActivity(sendIntent);
                else
                 Toast.makeText(v.getContext(),"No application available",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean isIntentAvailable(Intent intent){
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,0);
        boolean isIntentSafe = activities.size() > 0;
        return isIntentSafe;
    }
}