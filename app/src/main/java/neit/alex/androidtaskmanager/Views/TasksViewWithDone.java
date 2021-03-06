package neit.alex.androidtaskmanager.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import neit.alex.androidtaskmanager.Data.TaskDB;
import neit.alex.androidtaskmanager.R;
import neit.alex.androidtaskmanager.Scope.Task;
import neit.alex.androidtaskmanager.Views.Forms.NewTaskForm;

public class TasksViewWithDone extends AppCompatActivity {

    static final int NEW_TASK = 10;
    static final int TASK_INFO = 20;

    TaskDB db;
    ListView listView;

    SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
    static ArrayList<Task> tasks;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        db = new TaskDB(this);
        tasks = db.readAllWithDone();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NewTaskForm.class);
                startActivityForResult(i, NEW_TASK);
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(R.id.tasksView);

        for (int i=0; i<tasks.size(); i++) {
            adapter.add(tasks.get(i).getName() + "\nTask due by " + tasks.get(i).getDate() + " at " + tasks.get(i).getTime());
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),TaskInfo.class);

                intent.putExtra("selectedTask", tasks.get(i).getId());
                getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_show_done) {

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_TASK) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Toast notif = Toast.makeText(this, data.getStringExtra("return"), Toast.LENGTH_SHORT);
                notif.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
                notif.show();

                Intent intent = new Intent(getApplicationContext(),TasksViewWithDone.class);
                getApplicationContext().startActivity(intent);
            }
        }
    }
}
