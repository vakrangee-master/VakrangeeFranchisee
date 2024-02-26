package in.vakrangee.franchisee.bcadetails;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.vakrangee.franchisee.nextgenfranchiseeapplication.stepstepper.HeaderStepperAdapter;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.stepstepper.HeaderStepperDTO;
import in.vakrangee.franchisee.nextgenfranchiseeapplication.stepstepper.ifcStepperItemClick;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;

public class BCAHeaderStepperFragment extends BaseFragment {

    private HeaderStepperAdapter headerStepperAdapter;
    private RecyclerView recyclerView;
    private ArrayList<HeaderStepperDTO> headerStepperDTOS;
    private BCAEntryDetailsDto bcaEntryDetailsDto;

    public BCAHeaderStepperFragment() {
        // Required empty public constructor
    }

    public interface IHeaderClicks {

        public void onItemClick(int position, View v, ArrayList<HeaderStepperDTO> headerStepperDTO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_header_steps, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.horizontal_steeper_layout);

        return view;
    }

    private void setAdapter(final BCAHeaderStepperFragment.IHeaderClicks iHeaderClicks) {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        headerStepperDTOS = new ArrayList<HeaderStepperDTO>();
        for (int i = 0; i < BCAHeaderStepperFragment.MyData.name.length; i++) {
            String status = getStatus(BCAHeaderStepperFragment.MyData.Id[i]);
            boolean IsPartiallyFilled = false;
            boolean IsAllEnterValidated = false;

            if (status.equalsIgnoreCase("1"))
                IsPartiallyFilled = true;
            else if (status.equalsIgnoreCase("2"))
                IsAllEnterValidated = true;

            headerStepperDTOS.add(new HeaderStepperDTO(
                    BCAHeaderStepperFragment.MyData.name[i],
                    BCAHeaderStepperFragment.MyData.Id[i], IsPartiallyFilled, IsAllEnterValidated
            ));
        }

        headerStepperAdapter = new HeaderStepperAdapter(getContext(), headerStepperDTOS, new ifcStepperItemClick() {
            @Override
            public void onItemClick(int position, View v, ArrayList<HeaderStepperDTO> headerStepperDTO) {
                iHeaderClicks.onItemClick(position, v, headerStepperDTO);
            }
        });
        recyclerView.setAdapter(headerStepperAdapter);
    }

    public void refresh(BCAEntryDetailsDto entryDetailsDto, BCAHeaderStepperFragment.IHeaderClicks iHeaderClicks) {
        this.bcaEntryDetailsDto = entryDetailsDto;
        setAdapter(iHeaderClicks);
    }

    public String getStatus(String Id) {
        String status = "0";

        switch (Id) {

            case "1":
                status = TextUtils.isEmpty(bcaEntryDetailsDto.getBankInfoStatus()) ? "0" : bcaEntryDetailsDto.getBankInfoStatus();
                break;

            case "2":
                status = TextUtils.isEmpty(bcaEntryDetailsDto.getBasicInfoStatus()) ? "0" : bcaEntryDetailsDto.getBasicInfoStatus();
                break;

            case "3":
                status = TextUtils.isEmpty(bcaEntryDetailsDto.getOutletInfoStatus()) ? "0" : bcaEntryDetailsDto.getOutletInfoStatus();
                break;

            case "4":
                status = TextUtils.isEmpty(bcaEntryDetailsDto.getSupportingInfoStatus()) ? "0" : bcaEntryDetailsDto.getSupportingInfoStatus();
                break;

            case "5":
                status = TextUtils.isEmpty(bcaEntryDetailsDto.getOtherInfoStatus()) ? "0" : bcaEntryDetailsDto.getOtherInfoStatus();
                break;

            default:
                break;
        }
        return status;
    }

    public void notifyAdapter(int pos) {

        HeaderStepperDTO stepperDTO = headerStepperDTOS.get(pos);
        String s = getStatus(stepperDTO.getStpeerId());
        int status = TextUtils.isEmpty(s) ? 0 : Integer.parseInt(s);

        if (status == 1) {
            stepperDTO.setPartiallyfilled(true);

        } else if (status == 2) {
            stepperDTO.setAllEnterValidated(true);

        }

        headerStepperAdapter.notify(pos);
        scrollRecyclerView(pos);
    }

    public void scrollRecyclerView(int pos) {
        recyclerView.smoothScrollToPosition(pos);

    }

    //Custom data
    public static class MyData {
        static String[] name = {"Bank Information", "Basic Information", "Outlet Information", "Supporting Information", "Other Information"};
        static String[] Id = {"1", "2", "3", "4", "5"};
    }

    public void refreshBCAEntryDetailsDto(BCAEntryDetailsDto entryDetailsDto) {
        if (entryDetailsDto != null)
            this.bcaEntryDetailsDto = entryDetailsDto;
    }
}
