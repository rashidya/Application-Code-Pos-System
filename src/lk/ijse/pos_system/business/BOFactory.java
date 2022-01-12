package lk.ijse.pos_system.business;

import lk.ijse.pos_system.business.custom.Impl.ManageItemsBOImpl;
import lk.ijse.pos_system.business.custom.Impl.ManageOrderBOImpl;
import lk.ijse.pos_system.business.custom.Impl.PurchaseOrderBOImpl;
import lk.ijse.pos_system.business.custom.Impl.SystemReportsBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory(){

    }

    public static BOFactory getBoFactory(){
        if (boFactory==null){
            boFactory=new BOFactory();
        }
        return boFactory;
    }

    public SuperBO getBO(BOTypes boTypes){
        switch (boTypes){
            case ITEMS:return new ManageItemsBOImpl();
            case ORDER:return new ManageOrderBOImpl();
            case PURCHASE_ORDER:return new PurchaseOrderBOImpl();
            case SYSTEM_REPORTS:return new SystemReportsBOImpl();
            default:return null;
        }
    }

    public enum BOTypes{
        ITEMS,ORDER,PURCHASE_ORDER,SYSTEM_REPORTS
    }
}
