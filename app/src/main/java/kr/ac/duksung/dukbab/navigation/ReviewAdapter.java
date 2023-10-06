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
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            // 커서에서 데이터를 추출하고 ViewHolder 뷰에 바인딩합니다.
            String restaurantName = cursor.getString(cursor.getColumnIndex(ReviewDBOpenHelper.COLUMN_SELECTED_RESTAURANT));
            String menuName = cursor.getString(cursor.getColumnIndex(ReviewDBOpenHelper.COLUMN_MENU_NAME));
            float rating = cursor.getFloat(cursor.getColumnIndex(ReviewDBOpenHelper.COLUMN_RATING));
            String reviewContent = cursor.getString(cursor.getColumnIndex(ReviewDBOpenHelper.COLUMN_REVIEW_CONTENT));
            //String username = cursor.getString(cursor.getColumnIndex(ReviewDBOpenHelper.COLUMN_EMAIL)); // 이메일 가져오기
            String nickname = cursor.getString(cursor.getColumnIndex(ReviewDBOpenHelper.COLUMN_NICKNAME)); // 이메일 가져오기
            String createdDate = cursor.getString(cursor.getColumnIndex(ReviewDBOpenHelper.COLUMN_REVIEW_CREATED_DATE)); // 리뷰 작성 날짜 가져오기


            // 데이터를 ViewHolder 뷰에 바인딩합니다.
            holder.restaurantNameTextView.setText(restaurantName);
            holder.menuNameTextView.setText(menuName);
            holder.ratingTextView.setText(String.valueOf(rating));
            holder.reviewContentTextView.setText(reviewContent);
            holder.nicknameTextView.setText(nickname);
            holder.createdDateTextView.setText(createdDate);
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

