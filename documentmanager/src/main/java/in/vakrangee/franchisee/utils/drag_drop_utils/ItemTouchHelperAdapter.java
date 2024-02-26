package in.vakrangee.franchisee.utils.drag_drop_utils;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
