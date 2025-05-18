package com.example.chefvlogosss.ui.ownVlogs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chefvlogosss.R;
import com.example.chefvlogosss.WatchVlogActivity;
import com.example.chefvlogosss.data.model.VlogVideo;
import com.example.chefvlogosss.databinding.FragmentTransformBinding;
import com.example.chefvlogosss.databinding.ItemTransformBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
public class OwnVlogsFragment extends Fragment {

    private FragmentTransformBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTransformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerviewTransform;
        VlogAdapter adapter = new VlogAdapter();
        recyclerView.setAdapter(adapter);

// 🔥 Firebase lekérdezés
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("vlogs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<VlogVideo> vlogList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        VlogVideo vlog = doc.toObject(VlogVideo.class);
                        vlogList.add(vlog);
                    }
                    adapter.submitList(vlogList);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Lekérdezés sikertelen", e));

        //transformViewModel.getTexts().observe(getViewLifecycleOwner(), adapter::submitList);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    private class VlogAdapter extends ListAdapter<VlogVideo, TransformViewHolder> {

        protected VlogAdapter() {
            super(new DiffUtil.ItemCallback<VlogVideo>() {
                @Override
                public boolean areItemsTheSame(@NonNull VlogVideo oldItem, @NonNull VlogVideo newItem) {
                    return oldItem.getVlogId().equals(newItem.getVlogId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull VlogVideo oldItem, @NonNull VlogVideo newItem) {
                    return oldItem.equals(newItem);
                }
            });
        }

        @NonNull
        @Override
        public TransformViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTransformBinding binding = ItemTransformBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new TransformViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull TransformViewHolder holder, int position) {
            VlogVideo vlog = getItem(position);
            holder.textView.setText(vlog.getTitle());

            // Egy default kép - opcionálisan cserélhető thumbnail-re is, ha lesz.
            /*holder.imageView.setImageDrawable(
                    ResourcesCompat.getDrawable(holder.imageView.getResources(),
                            R.drawable.avatar_1, null));*/

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), WatchVlogActivity.class);
                intent.putExtra("video_link", vlog.getLink());  // Ha később használod
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }
    }

    private static class TransformViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;
        public final TextView textView;

        public TransformViewHolder(ItemTransformBinding binding) {
            super(binding.getRoot());
            imageView = binding.imageViewItemTransform;
            textView = binding.textViewItemTransform;
        }
    }

}