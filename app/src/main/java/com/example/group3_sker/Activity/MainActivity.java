package com.example.group3_sker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group3_sker.Adapter.PopularListAdapter;
import com.example.group3_sker.Domain.PopularDomain;
import com.example.group3_sker.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterPopular;
    private RecyclerView recyclerViewPopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        bottom_navigation();

    }

    private void bottom_navigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);

        homeBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        });

        cartBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });
    }

    private void initRecyclerView() {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("Macbook Pro", "In the enchanted realm of cosmic hammocks, interstellar sloths philosophized about the quantum mechanics of banana peels. Bubble-wrap galaxies popped with laughter, unraveling tapestries of celestial confetti. Meanwhile, disco ball fireflies synchronized their bioluminescent dances to the rhythm of stellar heartbeat echoes. Quantum unicorns leaped through wormhole rainbows, leaving trails of stardust graffiti on intergalactic graffiti walls. And amidst the cosmic siesta, time-traveling caterpillars spun quantum cocoons from moonbeam silk.", "pic1",15,4,400));
        items.add(new PopularDomain("Ps5","In the enchanted realm of cosmic hammocks, interstellar sloths philosophized about the quantum mechanics of banana peels. Bubble-wrap galaxies popped with laughter, unraveling tapestries of celestial confetti. Meanwhile, disco ball fireflies synchronized their bioluminescent dances to the rhythm of stellar heartbeat echoes. Quantum unicorns leaped through wormhole rainbows, leaving trails of stardust graffiti on intergalactic graffiti walls. And amidst the cosmic siesta, time-traveling caterpillars spun quantum cocoons from moonbeam silk.","pic2",10,5,500));
        items.add(new PopularDomain("Macbook Air","In the enchanted realm of cosmic hammocks, interstellar sloths philosophized about the quantum mechanics of banana peels. Bubble-wrap galaxies popped with laughter, unraveling tapestries of celestial confetti. Meanwhile, disco ball fireflies synchronized their bioluminescent dances to the rhythm of stellar heartbeat echoes. Quantum unicorns leaped through wormhole rainbows, leaving trails of stardust graffiti on intergalactic graffiti walls. And amidst the cosmic siesta, time-traveling caterpillars spun quantum cocoons from moonbeam silk.","pic3", 12,5,300));

        recyclerViewPopular=findViewById(R.id.view1);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterPopular = new PopularListAdapter(items);
        recyclerViewPopular.setAdapter(adapterPopular);
    }
}