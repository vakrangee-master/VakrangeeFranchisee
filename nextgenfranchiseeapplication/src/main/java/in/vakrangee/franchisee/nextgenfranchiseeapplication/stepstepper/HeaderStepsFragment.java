package in.vakrangee.franchisee.nextgenfranchiseeapplication.stepstepper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.vakrangee.franchisee.nextgenfranchiseeapplication.R;
import in.vakrangee.supercore.franchisee.baseutils.BaseFragment;
import in.vakrangee.supercore.franchisee.model.NextGenFranchiseeApplicationFormDto;

public class HeaderStepsFragment extends BaseFragment {

    private HeaderStepperAdapter headerStepperAdapter;
    private RecyclerView recyclerView;
    private ArrayList<HeaderStepperDTO> headerStepperDTOS;
    private NextGenFranchiseeApplicationFormDto applicationFormDto;

    public HeaderStepsFragment() {
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

    private void setAdapter(final IHeaderClicks iHeaderClicks) {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        headerStepperDTOS = new ArrayList<HeaderStepperDTO>();
        for (int i = 0; i < MyData.name.length; i++) {
            String status = getStatus(MyData.Id[i]);
            boolean IsPartiallyFilled = false;
            boolean IsAllEnterValidated = false;

            if (status.equalsIgnoreCase("1"))
                IsPartiallyFilled = true;
            else if (status.equalsIgnoreCase("2"))
                IsAllEnterValidated = true;

            headerStepperDTOS.add(new HeaderStepperDTO(
                    MyData.name[i],
                    MyData.Id[i], IsPartiallyFilled, IsAllEnterValidated
            ));
        }

        headerStepperAdapter = new HeaderStepperAdapter(getContext(), headerStepperDTOS,  new ifcStepperItemClick() {
            @Override
            public void onItemClick(int position, View v, ArrayList<HeaderStepperDTO> headerStepperDTO) {
                iHeaderClicks.onItemClick(position, v, headerStepperDTO);
            }
        });
        recyclerView.setAdapter(headerStepperAdapter);
    }

    public void refresh(NextGenFranchiseeApplicationFormDto formDto, IHeaderClicks iHeaderClicks) {
        this.applicationFormDto = formDto;
        setAdapter(iHeaderClicks);
    }

    public String getStatus(String Id) {
        String status = "0";

        switch (Id) {

            case "1":
                status = TextUtils.isEmpty(applicationFormDto.getFranchiseeDetailStatus()) ? "0" : applicationFormDto.getFranchiseeDetailStatus();
                break;

            case "2":
                status = TextUtils.isEmpty(applicationFormDto.getAddressStatus()) ? "0" : applicationFormDto.getAddressStatus();
                break;

            case "3":
                status = TextUtils.isEmpty(applicationFormDto.getContactInfoStatus()) ? "0" : applicationFormDto.getContactInfoStatus();
                break;

            case "4":
                status = TextUtils.isEmpty(applicationFormDto.getGeneralInfoStatus()) ? "0" : applicationFormDto.getGeneralInfoStatus();
                break;

            case "5":
                status = TextUtils.isEmpty(applicationFormDto.getBankDetailStatus()) ? "0" : applicationFormDto.getBankDetailStatus();
                break;

            case "6":
                status = TextUtils.isEmpty(applicationFormDto.getProposedVKDetailStatus()) ? "0" : applicationFormDto.getProposedVKDetailStatus();
                break;

            case "7":
                status = TextUtils.isEmpty(applicationFormDto.getReferencesStatus()) ? "0" : applicationFormDto.getReferencesStatus();
                break;

            case "8":
                status = TextUtils.isEmpty(applicationFormDto.getCriteriaStatus()) ? "0" : applicationFormDto.getCriteriaStatus();
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

        private MyData(){}

        static String[] name = {"Franchisee Details", "Franchisee Address Details", "Contact Information", "General Information", "Bank Details",
                "Proposed Vakrangee Kendra Location Details", "References", "Criteria"};
        static String[] Id = {"1", "2", "3", "4", "5", "6", "7", "8"};
    }

    public void refreshApplicationFormDto(NextGenFranchiseeApplicationFormDto appFormDto) {
        if (appFormDto != null)
            this.applicationFormDto = appFormDto;
    }

}

