package in.vakrangee.core.commongui.imagegallery;

import android.view.View;

public interface RecyclerViewClickLongClickListener {

    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
