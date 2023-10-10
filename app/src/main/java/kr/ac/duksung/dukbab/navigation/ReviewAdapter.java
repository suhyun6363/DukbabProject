package kr.ac.duksung.dukbab.navigation;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import kr.ac.duksung.dukbab.R;
import kr.ac.duksung.dukbab.db.ReviewDBOpenHelper;
import androidx.annotation.NonNull;
        
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Cursor cursor;

    public ReviewAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_item_layout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        cursor.moveToPosition(position);

        String selectedRestaurant = cursor.getString(cursor.getColumnIndexOrThrow(ReviewDBOpenHelper.COLUMN_SELECTED_RESTAURANT));
        String menuName = cursor.getString(cursor.getColumnIndexOrThrow(ReviewDBOpenHelper.COLUMN_MENU_NAME));
        float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(ReviewDBOpenHelper.COLUMN_RATING));
        String reviewContent = cursor.getString(cursor.getColumnIndexOrThrow(ReviewDBOpenHelper.COLUMN_REVIEW_CONTENT));
        String nickname = cursor.getString(cursor.getColumnIndexOrThrow(ReviewDBOpenHelper.COLUMN_NICKNAME));
        String createdDate = cursor.getString(cursor.getColumnIndexOrThrow(ReviewDBOpenHelper.COLUMN_REVIEW_CREATED_DATE));

        // 별 이모티콘을 텍스트뷰에 표시
        String starRating = getStarRatingString(rating);
        holder.ratingTextView.setText(starRating);

        holder.restaurantNameTextView.setText(selectedRestaurant);
        holder.menuNameTextView.setText(menuName);
        holder.reviewContentTextView.setText(reviewContent);
        holder.nicknameTextView.setText("작성자: " + nickname);
        holder.createdDateTextView.setText(createdDate);
    }

    private String getStarRatingString(float rating) {
        // 별 이모티콘을 rating 값에 따라 반환
        if (rating >= 4.5) {
            return "★★★★★";
        } else if (rating >= 3.5) {
            return "★★★★☆";
        } else if (rating >= 2.5) {
            return "★★★☆☆";
        } else if (rating >= 1.5) {
            return "★★☆☆☆";
        } else if (rating >= 0.5) {
            return "★☆☆☆☆";
        } else {
            return "☆☆☆☆☆";
        }
    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantNameTextView;
        TextView menuNameTextView;
        TextView ratingTextView;
        TextView reviewContentTextView;
        TextView nicknameTextView;
        TextView createdDateTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            restaurantNameTextView = itemView.findViewById(R.id.restaurantNameTextView);
            menuNameTextView = itemView.findViewById(R.id.menuNameTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            reviewContentTextView = itemView.findViewById(R.id.reviewContentTextView);
            nicknameTextView = itemView.findViewById(R.id.nicknameTextView);
            createdDateTextView = itemView.findViewById(R.id.createdDateTextView);
        }
    }
}

