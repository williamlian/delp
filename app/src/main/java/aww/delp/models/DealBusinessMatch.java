package aww.delp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "deal_business_match")
public class DealBusinessMatch extends Model {
    @Column(name = "deal_uuid")
    String dealUuid;
    @Column(name = "business_id")
    String businessId;

    public static String getBusinessId(String dealUuid) {
        DealBusinessMatch match = getByDealUuid(dealUuid);
        if(match != null) {
            return match.businessId;
        }
        return null;
    }

    public static void setBusinessId(String dealUuid, String businessId) {
        DealBusinessMatch match = getByDealUuid(dealUuid);
        if(match != null) {
            match.businessId = businessId;
        } else {
            match = new DealBusinessMatch();
            match.dealUuid = dealUuid;
            match.businessId = businessId;
        }
        match.save();
    }

    public static DealBusinessMatch getByDealUuid(String dealUuid) {
        List<DealBusinessMatch> matches = new Select().from(DealBusinessMatch.class).where("deal_uuid = ?", dealUuid).execute();
        if(matches.size() > 0) {
            return matches.get(0);
        }
        return null;
    }
}
