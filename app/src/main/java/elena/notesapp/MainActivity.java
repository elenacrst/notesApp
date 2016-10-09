package elena.notesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button add;
    private ListView titleList;
    public ArrayList<Notes> myNotes=new ArrayList<Notes>();
    private int num;
    private NotesAdapter adapter;
    public ArrayList<String> titles=new ArrayList<String>();
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add=(Button)findViewById(R.id.button);
        titleList=(ListView)findViewById(R.id.listView);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),Note.class);
                startActivity(i);
            }
        });

        saveInfo();

        titleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(Notes n:myNotes){
                    if(n.getTitle()== titles.get(position)){

                        Intent i=new Intent(view.getContext(),Note.class);
                        i.putExtra("title",n.getTitle());
                        i.putExtra("body",n.getBody());
                        startActivity(i);
                    }
                }
            }
        });
    }

    private void saveInfo(){
        Notes n=new Notes("","");

        sharedPreferences=getPreferences(Context.MODE_PRIVATE);
        num=sharedPreferences.getInt("number",-1);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        for(int i=0;i<=num;i++){
            String t,b;
            t=sharedPreferences.getString("titletext"+String.valueOf(i),"");
            b=sharedPreferences.getString("bodytext"+String.valueOf(i),"");
            Notes myNote=new Notes("","");
            myNote.setTitle(t);
            myNote.setBody(b);
            myNotes.add(i,myNote);
            titles.add(i,myNote.getTitle());
        }

        if(this.getIntent().hasExtra("KeyTitle")&&this.getIntent().hasExtra("KeyBody"))
        {  if(this.getIntent().getExtras().getInt("modify")==0)

        {
            n.setTitle(this.getIntent().getExtras().getString("KeyTitle"));
            n.setBody(this.getIntent().getExtras().getString("KeyBody"));

            num++;

            editor.putInt("number",num);
            editor.commit();

            for(int x=num;x>0;x--){
                editor.putString("titletext"+String.valueOf(x),myNotes.get(x-1).getTitle());
                editor.commit();
                editor.putString("bodytext"+String.valueOf(x),myNotes.get(x-1).getBody());
                editor.commit();
                if(x==num){
                    myNotes.add(x,myNotes.get(x-1));
                    titles.add(x,titles.get(x-1));
                }
                else
                {
                    myNotes.set(x,myNotes.get(x-1));
                    titles.set(x,titles.get(x-1));
                }
            }

            editor.putString("titletext"+String.valueOf(0),n.getTitle());
            editor.commit();
            editor.putString("bodytext"+String.valueOf(0),n.getBody());
            editor.commit();
            if(titles.size()==0){
                myNotes.add(0,n);
                titles.add(0,n.getTitle());
            }
            else{
                myNotes.set(0,n);
                titles.set(0,n.getTitle());
            }

        }
        else if(this.getIntent().getExtras().getInt("modify")==1)
            {
                String modifiedAt=this.getIntent().getExtras().getString("at");
                Toast.makeText(this, "Modified", Toast.LENGTH_SHORT).show();

                int i=search(modifiedAt);

                for(int x=i;x>0;x--){
                    editor.putString("titletext"+String.valueOf(x),myNotes.get(x-1).getTitle());
                    editor.commit();
                    editor.putString("bodytext"+String.valueOf(x),myNotes.get(x-1).getBody());
                    editor.commit();

                    myNotes.set(x,myNotes.get(x-1));
                    titles.set(x,titles.get(x-1));
                }

                n.setTitle(this.getIntent().getExtras().getString("KeyTitle"));
                n.setBody(this.getIntent().getExtras().getString("KeyBody"));

                editor.putString("titletext"+String.valueOf(0),n.getTitle());
                editor.commit();
                editor.putString("bodytext"+String.valueOf(0),n.getBody());
                editor.commit();

                myNotes.set(0,n);
                titles.set(0,n.getTitle());
            }
        }

        if(this.getIntent().hasExtra("KeyTitle")&&this.getIntent().getExtras().getInt("modify")==3){
            String remove=this.getIntent().getExtras().getString("KeyTitle");
            int i=search(remove);
            for(int x=i;x<num;x++){
                editor.putString("titletext"+String.valueOf(x),myNotes.get(x+1).getTitle());
                editor.commit();
                editor.putString("bodytext"+String.valueOf(x),myNotes.get(x+1).getBody());
                editor.commit();

                myNotes.set(x,myNotes.get(x+1));
                titles.set(x,titles.get(x+1));
            }
            myNotes.remove(num);
            titles.remove(num);
            num--;
            editor.putInt("number",num);
            editor.commit();
        }

        if(myNotes.size()>0) {
            adapter=new NotesAdapter(getApplicationContext(),R.layout.row,titles);

            titleList.setAdapter(adapter);
        }
    }
    public int search(String title){

        for(int i=0;i<titles.size();i++)
            if(title.equals(titles.get(i)))
                return i;
        return -1;
    }
}