package in.vakrangee.franchisee.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.vakrangee.franchisee.R;

/**
 * Created by Nileshd on 11/12/2016.
 */
public class FragmentAction  extends Fragment {

    private static final String KEY_MOVIE_TITLE = "key_title";

    public FragmentAction() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment FragmentAction.
     */
    public static FragmentAction newInstance(String movieTitle) {
        FragmentAction fragmentAction = new FragmentAction();
        Bundle args = new Bundle();
        args.putString(KEY_MOVIE_TITLE, movieTitle);
        fragmentAction.setArguments(args);

        return fragmentAction;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_action, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      /*  Drawable movieIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.add_marker, getContext().getTheme());
        if (movieIcon != null) {
            movieIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
        }
        ((ImageView) view.findViewById(R.id.movie_icon)).setImageDrawable(movieIcon);
*/
        String movieTitle = getArguments().getString(KEY_MOVIE_TITLE);
        ((TextView) view.findViewById(R.id.movie_title)).setText(movieTitle);
    }
}
