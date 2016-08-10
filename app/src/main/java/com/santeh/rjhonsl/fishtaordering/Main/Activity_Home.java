package com.santeh.rjhonsl.fishtaordering.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;

/**
 * Created by rjhonsl on 5/31/2016.
 */
public class Activity_Home extends AppCompatActivity {

    Activity activity;
    Context context;
    DBaseQuery db;

    Button btnConvert;
    EditText edtToConvert, edtConverted, edtBinary;

    private TextView btnSendOrder, btnOrdHistory, btnSettings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginscreen);
        activity = this;
        context = Activity_Home.this;

        db = new DBaseQuery(this);
        db.open();


        btnSendOrder = (TextView) findViewById(R.id.btnSendOrder);
        btnOrdHistory = (TextView) findViewById(R.id.btnOrderHistory);
        btnSettings = (TextView) findViewById(R.id.btnSettings);

        edtToConvert = (EditText) findViewById(R.id.edtToConvert);
        edtBinary = (EditText) findViewById(R.id.edtBinary);
        edtConverted = (EditText) findViewById(R.id.edtConverted);

        btnConvert = (Button) findViewById(R.id.btnEncrypt);


        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                String hexed = Helper.convert.stringtoHex(edtToConvert.getText().toString());
//                String binned = Helper.convert.StringToBinary(edtToConvert.getText().toString()).toString();
//                String stringFromHexedFromBinned = Helper.convert.stringtoHex(binned);
////                String reversedHex = Helper.convert.toStringBuilder(hexed).reverse().toString();
//
                edtBinary.setText(hexed);
//
////                edtConverted.setText(Helper.convert.HextoString(Helper.convert.stringtoHex(edtToConvert.getText().toString())));
////                edtBinary.setText(convertToBinary(edtToConvert.getText().toString()+""));
////                String reversedbinary = convertToBinary(edtToConvert.getText().toString()+"").reverse().toString();
////                String binaryTOChar = convertBinaryToChar(
//////                        convertToBinary(edtToConvert.getText().toString()).toString()
////                        reversedbinary
////                );
////
////                edtBinary.setText(edtBinary.getText().toString()+"\n\n"+reversedbinary);
//
////                edtConverted.setText(Helper.convert.HextoString(stringFromHexedFromBinned));

            }
        });

        btnSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });


        btnOrdHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Activity_OrderHistory.class);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Activity_Settings.class);
                startActivity(intent);
            }
        });


        if (db.getAllItems().size() < 1){
            insertItems();
        }

        if (db.getAllCustomers().size() < 1){
            Helper.toast.indefinite(activity, "Hey");
            insertCustomers();
        }

    }


    private void checkIfFreshInstall() {
        if (db.getSettingsCount()< 1){
            Helper.ActivityAction.startActivityClearStack(activity, Activity_Welcome.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfFreshInstall();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void insertItems(){
//        Log.d("DB", "INSERTING ITEMS");

        String[] itemss = getResources().getStringArray(R.array.items);
        String[] units = getResources().getStringArray(R.array.itm_units);
        String[] group_code = getResources().getStringArray(R.array.group_code);

        for (int i = 0; i < itemss.length; i++) {
            db.insertItems(i + "", itemss[i], group_code[i], units[i], "1");
        }

    }



    private void insertCustomers() {
        db.rawQuery("" +

                "INSERT INTO `tbl_cust` (cust_id, cust_code, cust_name, cust_type, cust_isactive) VALUES \n" +
                " ('1','METROS','METRO SUPERMART','OUTLET','1'),\n" +
                " ('2','PGALAB','PG-ALABANG','OUTLET','1'),\n" +
                " ('3','PGBINA','PG-BINAN','OUTLET','1'),\n" +
                " ('4','PGLIBE','PG-LIBERTAD','OUTLET','1'),\n" +
                " ('5','PGMONU','PG-MONUMENTO','OUTLET','1'),\n" +
                " ('6','PGPARA','PG-PARANAQUE','OUTLET','1'),\n" +
                " ('7','PGSAND','PG-SAN DIONISIO','OUTLET','1'),\n" +
                " ('8','PGSANP','PG-SAN PEDRO','OUTLET','1'),\n" +
                " ('9','PGSHAW','PG-SHAW','OUTLET','1'),\n" +
                " ('10','PGSTAM','PG-STA. MESA','OUTLET','1'),\n" +
                " ('11','PGSUCA','PG-SUCAT','OUTLET','1'),\n" +
                " ('12','PGVALE','PG-VALENZUELA','OUTLET','1'),\n" +
                " ('13','PIONEE','PIONEER CENTER','OUTLET','1'),\n" +
                " ('14','PRODUC','PRODUCT DEV''T','OUTLET','1'),\n" +
                " ('15','RSCANG','RSC-ANGELES','OUTLET','1'),\n" +
                " ('16','RSCBAL','RSC-BALAGTAS','OUTLET','1'),\n" +
                " ('17','RSCBFH','RSC-BF HOMES','OUTLET','1'),\n" +
                " ('18','RSCCAL','RSC-CALIFORNIA SQUARE','OUTLET','1'),\n" +
                " ('19','RSCCON','RSC-CONGRESSIONAL','OUTLET','1'),\n" +
                " ('20','RSCERM','RSC-ERMITA','OUTLET','1'),\n" +
                " ('21','RSCGAL','RSC-GALLERIA','OUTLET','1'),\n" +
                " ('22','RSCMAL','RSC-MALOLOS','OUTLET','1'),\n" +
                " ('23','RSCMET','RSC-METRO EAST','OUTLET','1'),\n" +
                " ('24','RSCNEP','RSC-NEPO','OUTLET','1'),\n" +
                " ('25','RSCPUL','RSC-PULILAN','OUTLET','1'),\n" +
                " ('26','RSCSAN','RSC-SAN FERNANDO','OUTLET','1'),\n" +
                " ('27','SOUTHS','SOUTH SUPT. -VALENZUELA','OUTLET','1'),\n" +
                " ('28','WMMAKA','WM-MAKATI','OUTLET','1'),\n" +
                " ('29','WMEROD','WM-E.RODRIGUEZ','OUTLET','1'),\n" +
                " ('30','WMNORT','WM-NORTH EDSA','OUTLET','1'),\n" +
                " ('31','WMSANF','WM-SAN FERNANDO','OUTLET','1'),\n" +
                " ('32','WMSTAM','WM-STA. MARIA','OUTLET','1'),\n" +
                " ('33','WMTANA','WM-TANAUAN','OUTLET','1'),\n" +
                " ('34','PGBET','PG-BETTER LIVING','OUTLET','1'),\n" +
                " ('35','PGGUIG','PG-GUIGINTO','OUTLET','1'),\n" +
                " ('36','PG-QI','PG-QI','OUTLET','1'),\n" +
                " ('37','ESSEL','ESSEL SUPERMARKET','OUTLET','1'),\n" +
                " ('38','PG-PASO','PG-PASO DE BLAS','OUTLET','1'),\n" +
                " ('39','RSC-LG','RSC-LUCKY GOLD','OUTLET','1'),\n" +
                " ('40','RSCNUV','RSC-NUVALI','OUTLET','1'),\n" +
                " ('41','RSCSTAR','RSC-STA. ROSA','OUTLET','1'),\n" +
                " ('42','RSCTAR','RSC-TARGET MALL','OUTLET','1'),\n" +
                " ('43','RSCMER','RSC-MERVILLE','OUTLET','1'),\n" +
                " ('44','RSCPIO','RSC-FORUM PIONEER','OUTLET','1'),\n" +
                " ('45','RSCUPL','RSC-UP LOS BANOS','OUTLET','1'),\n" +
                " ('46','RSCSTA','RSC-STAR MALL','OUTLET','1'),\n" +
                " ('47','RSCCAN','RSC-CANLUBANG','OUTLET','1'),\n" +
                " ('48','RSCMERC','RSC-MERCEDES','OUTLET','1'),\n" +
                " ('49','WMSUCA','WM-SUCAT','OUTLET','1'),\n" +
                " ('50','RSCMAG','RSC-MAGNOLIA','OUTLET','1'),\n" +
                " ('51','PGPION','PG-PIONEER','OUTLET','1'),\n" +
                " ('52','WMGUIG','WM-GUIGUINTO','OUTLET','1'),\n" +
                " ('53','RSCLIB','RSC-LIBIS','OUTLET','1'),\n" +
                " ('54','RSCTAN','RSC-TANDANG SORA','OUTLET','1'),\n" +
                " ('55','WMCALA','WM-CALAMBA','OUTLET','1'),\n" +
                " ('56','WMBICU','WM-BICUTAN','OUTLET','1'),\n" +
                " ('57','RSCMEY','RSC-MEYCAUAYAN','OUTLET','1'),\n" +
                " ('58','RSCBAN','RSC-BANAWE','OUTLET','1'),\n" +
                " ('59','RSC-CAB','RSC CABUYAO','OUTLET','1'),\n" +
                " ('60','WM-CABUYAO','WM CABUYAO','OUTLET','1'),\n" +
                " ('61','RSCFAU','RSC-FAUSTA','OUTLET','1'),\n" +
                " ('62','RSCSPA','RSC-SPARK','OUTLET','1'),\n" +
                " ('63','RSCFED','RSC-FEDERAL BAY','OUTLET','1'),\n" +
                " ('64','RSCLAS','RSC-LAS PINAS','OUTLET','1'),\n" +
                " ('65','RSCFORB','RSC-FORBES','OUTLET','1'),\n" +
                " ('66','RSCEAS','RSC-EASYMART','OUTLET','1'),\n" +
                " ('67','WMTAGA','WM-TAGAYTAY','OUTLET','1'),\n" +
                " ('68','CROSSUP','CROSSING DEPARTMENT STORE CORP','OUTLET','1'),\n" +
                " ('69','RSCMAD','RSC-MADISON GALLERIES','OUTLET','1'),\n" +
                " ('70','RSCMCK','RSC-MCKINLEY','OUTLET','1'),\n" +
                " ('71','WMCARM','WM-CARMONA','OUTLET','1'),\n" +
                " ('72','WMSTAR','WM-STA ROSA','OUTLET','1'),\n" +
                " ('73','WMDASM','WM-DASMARINAS','OUTLET','1'),\n" +
                " ('74','RSCACA','RSC-ACACIA ESCALADES','OUTLET','1'),\n" +
                " ('75','PGTERRA','PG-TERRACES','OUTLET','1'),\n" +
                " ('76','PGSANJ','PG-SAN JOSE DEL MONTE','OUTLET','1'),\n" +
                " ('77','PGSANRA','PG-SAN RAFAEL MONTALBAN','OUTLET','1'),\n" +
                " ('78','AANI','AANI','OUTLET','1'),\n" +
                " ('79','RSCGUA','RSC-GUAGUA','OUTLET','1'),\n" +
                " ('80','ADARLU','ADARME, LU','DIRECT','1'),\n" +
                " ('81','ALADAL','ALAS, DALMACIO','DIRECT','1'),\n" +
                " ('82','ALADOL','ALAS, DOLI','DIRECT','1'),\n" +
                " ('83','ANGKIT','ANGEL''S KITCHEN','DIRECT','1'),\n" +
                " ('84','ANGTON','ANG, TONY','DIRECT','1'),\n" +
                " ('85','ANGWIL','ANG, WILLIAM','DIRECT','1'),\n" +
                " ('86','ASUVEL','ASUNCION, VELLE','DIRECT','1'),\n" +
                " ('87','BACJIM','BACUD, JIMMY','DIRECT','1'),\n" +
                " ('88','BACJUN','BACELA, JUN','DIRECT','1'),\n" +
                " ('89','BALANA','BALAONZO, ANA','DIRECT','1'),\n" +
                " ('90','BALGRA','BALLERA, GRACE','DIRECT','1'),\n" +
                " ('91','BANEDW','BANGCAL, EDWIN','DIRECT','1'),\n" +
                " ('92','BANERW','BANGCAL, ERWIN','DIRECT','1'),\n" +
                " ('93','BANLEA','ARNZ FOOD INC.','DIRECT','1'),\n" +
                " ('94','BANWIL','BANAS, WILMA','DIRECT','1'),\n" +
                " ('95','BDOEMP','BDO EMPLOYEE','DIRECT','1'),\n" +
                " ('96','BEMLEE','BEMBO, LEE','DIRECT','1'),\n" +
                " ('97','BETHAN','BETHANY','DIRECT','1'),\n" +
                " ('98','BFAEXI','BFAR EXIIBIT','DIRECT','1'),\n" +
                " ('99','BO FOR','BO FOR DISPOSAL','DIRECT','1'),\n" +
                " ('100','BOLCON','BOLO, CONNIE','DIRECT','1'),\n" +
                " ('101','BOMCON','BOMER-PASSAN CONSIGNACION','DIRECT','1'),\n" +
                " ('102','CABART','CABIDOG, ARTHUR','DIRECT','1'),\n" +
                " ('103','CABDAN','CABRIERA, DANIEL','DIRECT','1'),\n" +
                " ('104','CANAUR','CANTOMAYOR, AURA','DIRECT','1'),\n" +
                " ('105','CAPERI','CAPISTRANO, ERIC','DIRECT','1'),\n" +
                " ('106','CASH','CASH SALES','DIRECT','1'),\n" +
                " ('107','CAUMAR','CAUDINA, MARGIE','DIRECT','1'),\n" +
                " ('108','CAURUD','CAUDENA, RUDY','DIRECT','1'),\n" +
                " ('109','CERDOL','CERIALES, DOLORES','DIRECT','1'),\n" +
                " ('110','CERJON','CERIALES, JONATHAN','DIRECT','1'),\n" +
                " ('111','CERMAR','CERIALES, MARK','DIRECT','1'),\n" +
                " ('112','CHAJES','CHAM, JESS','DIRECT','1'),\n" +
                " ('113','CHALAR','CHAN, LARRY','DIRECT','1'),\n" +
                " ('114','CHAWAN','CHAI, WAN','DIRECT','1'),\n" +
                " ('115','CORAAR','CORONEL, AARONDELL','DIRECT','1'),\n" +
                " ('116','CORMER','CORNELIA, MERLY','DIRECT','1'),\n" +
                " ('117','CRUBAY','CRUZ, BAYANI','DIRECT','1'),\n" +
                " ('118','CRUBOY','CRUZ, BOYET','DIRECT','1'),\n" +
                " ('119','CUANIC','CUAL, NICK','DIRECT','1'),\n" +
                " ('120','CUNRAC','CUNANAN, RACHELLE','DIRECT','1'),\n" +
                " ('121','DADREY','DADO, REY','DIRECT','1'),\n" +
                " ('122','DE TOR','DE TORRES, GILBERT','DIRECT','1'),\n" +
                " ('123','DELCRU','DELA CRUZ, JOEL','DIRECT','1'),\n" +
                " ('124','DEMERN','DEMALATA, ERNIE','DIRECT','1'),\n" +
                " ('125','DIGBOY','DIGNADICE, BOYET','DIRECT','1'),\n" +
                " ('126','DIGRIC','DIGNADICI, RICKY','DIRECT','1'),\n" +
                " ('127','DIGRIZ','DIGNADICE, RIZALDY','DIRECT','1'),\n" +
                " ('128','DORJOE','DORDAS, JOEL','DIRECT','1'),\n" +
                " ('129','DUMAMY','DOMONDON, AMY','DIRECT','1'),\n" +
                " ('130','DUMBON','DUMANGON, BONNIE','DIRECT','1'),\n" +
                " ('131','EMPLOYEES','EMPLOYEES','DIRECT','1'),\n" +
                " ('132','ERIC','ERIC','DIRECT','1'),\n" +
                " ('133','EVDIAP','EV DIAZ PRINTING','DIRECT','1'),\n" +
                " ('134','FISAQU','FISHWELTH AQUA','DIRECT','1'),\n" +
                " ('135','FISHMOKO','FISHMOKO','DIRECT','1'),\n" +
                " ('136','FISREP','FISHTA REPRESENTATION','DIRECT','1'),\n" +
                " ('137','FLOTHE','FLORES, THESS','DIRECT','1'),\n" +
                " ('138','FRABET','FRANCISCO, BETH','DIRECT','1'),\n" +
                " ('139','FRADEN','FRANCISICO, DENNIS','DIRECT','1'),\n" +
                " ('140','GABBOB','GABE, BOBBY','DIRECT','1'),\n" +
                " ('141','GAILIT','GAITAN, LITO','DIRECT','1'),\n" +
                " ('142','GARYEY','GARIANDO, YEYE','DIRECT','1'),\n" +
                " ('143','GERAID','GERONIMO, AIDA','DIRECT','1'),\n" +
                " ('144','GKSANTEH','GK-SANTEH','DIRECT','1'),\n" +
                " ('145','GOELLE','GO, ELLERY','DIRECT','1'),\n" +
                " ('146','GROACE','GROSPE, ACE','DIRECT','1'),\n" +
                " ('147','GROELI','GROSPE ELIZABETH','DIRECT','1'),\n" +
                " ('148','HAPHAU','HAPPY HAUZ','DIRECT','1'),\n" +
                " ('149','HONEME','HONRA, EMERSON','DIRECT','1'),\n" +
                " ('150','INOLOR','INONCELIO, LORD JAYSON','DIRECT','1'),\n" +
                " ('151','JALDAR','JALAGAT, DARYL','DIRECT','1'),\n" +
                " ('152','JARBOY','JARANILLA, BOY','DIRECT','1'),\n" +
                " ('153','JAVJUN','JAVIER, JUN','DIRECT','1'),\n" +
                " ('154','JOSELL','JOSE, ELLY CRIS','DIRECT','1'),\n" +
                " ('155','JOTCES','JOTOJOT, CESAR','DIRECT','1'),\n" +
                " ('156','JUAROM','JUADIAN, ROMEO','DIRECT','1'),\n" +
                " ('157','KATMEL','KATULOS, MELCHOR','DIRECT','1'),\n" +
                " ('158','KWTPHI','KWT PHILIPPINES','DIRECT','1'),\n" +
                " ('159','LAGMAR','LAGROSAS, MARIBEL','DIRECT','1'),\n" +
                " ('160','LAPCEL','LAPUS, CELSO','DIRECT','1'),\n" +
                " ('161','LAUROS','LAURIO, ROSE','DIRECT','1'),\n" +
                " ('162','LINSHE','LINGAN, SHERYL','DIRECT','1'),\n" +
                " ('163','LLACLA','LLANTO, CLAUDY','DIRECT','1'),\n" +
                " ('164','LLIALA','LLIANAS - ALABANG','DIRECT','1'),\n" +
                " ('165','LLIEVA','LLIANAS - EVACOM','DIRECT','1'),\n" +
                " ('166','LOCBOY','LOCINO, BOY','DIRECT','1'),\n" +
                " ('167','MAACOR','MAAGHOP, CORNELIO','DIRECT','1'),\n" +
                " ('168','MANBET','MANALASTAS, BETH','DIRECT','1'),\n" +
                " ('169','MANJUN','MANANSALA, JUN','DIRECT','1'),\n" +
                " ('170','MANLES','MANLANGIT, LESLY','DIRECT','1'),\n" +
                " ('171','MANVIC','MANALO, VICTOR','DIRECT','1'),\n" +
                " ('172','MARLIV','MARINE LIVE','DIRECT','1'),\n" +
                " ('173','MARMAR','MARGANA, MARK','DIRECT','1'),\n" +
                " ('174','MARNIE','MARQUEZ, NIEL','DIRECT','1'),\n" +
                " ('175','MENJEN','MENDANO, JENIFER','DIRECT','1'),\n" +
                " ('176','MIRPAU','MIRANDA, PAUL','DIRECT','1'),\n" +
                " ('177','MONCOR','MONFORT, CORINA','DIRECT','1'),\n" +
                " ('178','MONVIC','MONTERO, VICTOR SG','DIRECT','1'),\n" +
                " ('179','MR.BOL','MR. BOLO','DIRECT','1'),\n" +
                " ('180','MRSCRUZ','MRS.CRUZ','DIRECT','1'),\n" +
                " ('181','MRSDIA','MRS. DIAZ','DIRECT','1'),\n" +
                " ('182','MRYEAN','MR. YEAN','DIRECT','1'),\n" +
                " ('183','NACDEL','NACOLANGA, DELFIN','DIRECT','1'),\n" +
                " ('184','NALGEL','NALICA, GELBERT','DIRECT','1'),\n" +
                " ('185','NEAJOH','NEALEGA, JOHN','DIRECT','1'),\n" +
                " ('186','NEMRAM','NEMIS, RAMON','DIRECT','1'),\n" +
                " ('187','NOMKEV','NOMOROSA, KEVIN','DIRECT','1'),\n" +
                " ('188','NORDHO','NORIEGA, DHONNA','DIRECT','1'),\n" +
                " ('189','NORDON','NORIEGA, DONNA','DIRECT','1'),\n" +
                " ('190','OLCLOU','OLCHONDRA, LOURDES','DIRECT','1'),\n" +
                " ('191','OLIIMI','OLIMPO, IMILDA','DIRECT','1'),\n" +
                " ('192','ONGLUC','ONG, LUCY','DIRECT','1'),\n" +
                " ('193','ONGPHI','ONG, PHILIP','DIRECT','1'),\n" +
                " ('194','ONONOR','ONOR, NORALYN','DIRECT','1'),\n" +
                " ('195','ORDBOB','ORDONEZ, BOBBY','DIRECT','1'),\n" +
                " ('196','OXYORO','OXYGEN, ORO','DIRECT','1'),\n" +
                " ('197','PABELV','PABALI, ELVINO','DIRECT','1'),\n" +
                " ('198','PALGLO','PALISOC, GLORIA','DIRECT','1'),\n" +
                " ('199','PALMAG','PALA, MAGS','DIRECT','1'),\n" +
                " ('200','PANLEE','PANGILINAN, LEENYL','DIRECT','1'),\n" +
                " ('201','PARLAS','PARCO - LAS PINAS','DIRECT','1'),\n" +
                " ('202','PARMAR','PARENA, MARJAN','DIRECT','1'),\n" +
                " ('203','PELANG','PELAEZ, ANGELITO','DIRECT','1'),\n" +
                " ('204','PELJOS','PELEA, JOSEPH','DIRECT','1'),\n" +
                " ('205','PERJES','PEREZ, JESSIE','DIRECT','1'),\n" +
                " ('206','PERMAT','PERIALES, MATTY','DIRECT','1'),\n" +
                " ('207','PGQIAN','PG QI/CO ANDY','DIRECT','1'),\n" +
                " ('208','PLAGES','PLATON, GESELA','DIRECT','1'),\n" +
                " ('209','PRIV.','PRIETO, V.','DIRECT','1'),\n" +
                " ('210','PURDIR','PG-DIRECT','DIRECT','1'),\n" +
                " ('211','PUYROM','PUYAT, ROMULO','DIRECT','1'),\n" +
                " ('212','QUECON','QUE, CONRAD','DIRECT','1'),\n" +
                " ('213','RAMCOR','RAMOS, CORA','DIRECT','1'),\n" +
                " ('214','RAMREN','RAMPAS, RENZ','DIRECT','1'),\n" +
                " ('215','REBDIA','REBULTAN, DIANA','DIRECT','1'),\n" +
                " ('216','REBJEF','REBUYON, JEFFREY','DIRECT','1'),\n" +
                " ('217','REYEMM','REYES, EMMY','DIRECT','1'),\n" +
                " ('218','REYHEL','REYES, HELEN','DIRECT','1'),\n" +
                " ('219','RICPAT','RICO, PATRICIA','DIRECT','1'),\n" +
                " ('220','RITDEN','RITO, DENNIS','DIRECT','1'),\n" +
                " ('221','RKSCON','PRINCE RKS CONSIGNACION','DIRECT','1'),\n" +
                " ('222','ROBJOH','ROBIO, JOHN REY','DIRECT','1'),\n" +
                " ('223','ROBMIL','ROBLES, MILLE','DIRECT','1'),\n" +
                " ('224','RUBJOH','RUBIO, JOHN REY','DIRECT','1'),\n" +
                " ('225','SALANN','SALCEDO, ANNE','DIRECT','1'),\n" +
                " ('226','SALJOE','SALVACION, JOEL','DIRECT','1'),\n" +
                " ('227','SAMRAN','SAMOY, RANDY','DIRECT','1'),\n" +
                " ('228','SANCOO','SANTEH COOP','DIRECT','1'),\n" +
                " ('229','SANFEE','SANTEH FEEDS CORP','DIRECT','1'),\n" +
                " ('230','SANJOS','SANTOS, JOSELITO','DIRECT','1'),\n" +
                " ('231','SANJUA','SAN JUAN, RIZA','DIRECT','1'),\n" +
                " ('232','SANROS','SANTOS, ROSE','DIRECT','1'),\n" +
                " ('233','SANRUT','SANDAGON, RUTH','DIRECT','1'),\n" +
                " ('234','SISROS','SISON, ROSABEL','DIRECT','1'),\n" +
                " ('235','SOULE','SOUFFL, LE','DIRECT','1'),\n" +
                " ('236','SUPERB','SUPERB COLD STORAGE','DIRECT','1'),\n" +
                " ('237','TAGALD','TAGLE, ALDRINE','DIRECT','1'),\n" +
                " ('238','TANDAI','TANGI, DAISY','DIRECT','1'),\n" +
                " ('239','TANHEN','TAN, HENRY MORALES','DIRECT','1'),\n" +
                " ('240','TANRAU','TANGLAO, RAUL','DIRECT','1'),\n" +
                " ('241','TANWAR','TAN, WARREN','DIRECT','1'),\n" +
                " ('242','TAOALM','TAOT, ALMA','DIRECT','1'),\n" +
                " ('243','TAUJUV','TAUTO-AN, JUVETH','DIRECT','1'),\n" +
                " ('244','TOLMAR','TOLENTINO, MARICEL','DIRECT','1'),\n" +
                " ('245','TRUEMP','TRUCKER EMPLOYEE','DIRECT','1'),\n" +
                " ('246','TUSALD','TUSCANO, ALDRIN','DIRECT','1'),\n" +
                " ('247','TYJAME','TY, JAMES','DIRECT','1'),\n" +
                " ('248','UYSHIE','UY, SHIERRA','DIRECT','1'),\n" +
                " ('249','VELJOS','VELASCO, JOSEPH','DIRECT','1'),\n" +
                " ('250','VICVIC','VICENTE, VINCENT','DIRECT','1'),\n" +
                " ('251','VIFICE','VIFEL ICE PLANT','DIRECT','1'),\n" +
                " ('252','WESSTA','WEST STAFF','DIRECT','1'),\n" +
                " ('253','YADALM','YADAO, ALMIRA','DIRECT','1'),\n" +
                " ('254','YAPJER','YAP, JEREMY','DIRECT','1'),\n" +
                " ('255','PGNOVA','PG-NOVALICHES','DIRECT','1'),\n" +
                " ('256','ARAJOY','ARAGON, JOYCE','DIRECT','1'),\n" +
                " ('257','LEEDIC','LEE, MR. DICK','DIRECT','1'),\n" +
                " ('258','NUMFOO','NUMENORIN FOODS','DIRECT','1'),\n" +
                " ('259','TANELI','TAN, ELIZA','DIRECT','1'),\n" +
                " ('260','SEPAVE','SEPILLO, AVELINA','DIRECT','1'),\n" +
                " ('261','FOOBOW','FOOD BOWL','DIRECT','1'),\n" +
                " ('262','BURMU1','MUSHROOM BURGER','DIRECT','1'),\n" +
                " ('263','BURMU2','MUSHROOM BURGER 2','DIRECT','1'),\n" +
                " ('264','CAIJER','CAISIP, JERRY','DIRECT','1'),\n" +
                " ('265','CASCRI','CASTRO, CRISTITO','DIRECT','1'),\n" +
                " ('266','CHUCON','CHUA, CONSTANTINE','DIRECT','1'),\n" +
                " ('267','ALURAF','ALUNAN, RAFFY','DIRECT','1'),\n" +
                " ('268','FBMERK','FOODBOWL MERKATO','DIRECT','1'),\n" +
                " ('269','DONICE','DONG - ICE','DIRECT','1'),\n" +
                " ('270','ADADON','ADAYO, DON','DIRECT','1'),\n" +
                " ('271','SILAPA','SILAPAN - SECURITY GUARD','DIRECT','1'),\n" +
                " ('272','BAYEMP','BAYAYA EMPLOYEE','DIRECT','1'),\n" +
                " ('273','UYRICH','UY, RICHARD','DIRECT','1'),\n" +
                " ('274','TRARIV','RIVERBOOK TRADING INC.','DIRECT','1'),\n" +
                " ('275','DORELI','DOROTAN, ELIGIO','DIRECT','1'),\n" +
                " ('276','ISRJED','ISRAEL, JED','DIRECT','1'),\n" +
                " ('277','PASPRO','PASTEL DELI PRODUCTS','DIRECT','1'),\n" +
                " ('278','YANREN','RENEI YANONG','DIRECT','1'),\n" +
                " ('279','FANOMA','FANOMART QC CIRCLE','DIRECT','1'),\n" +
                " ('280','CELRON','RONIE CELORICO','DIRECT','1'),\n" +
                " ('281','SANMYR','SANTOS, MYRLA','DIRECT','1'),\n" +
                " ('282','ESRSEA','ESR SEAFOODS','DIRECT','1'),\n" +
                " ('283','TANWIL','WILLIAM TAN','DIRECT','1'),\n" +
                " ('284','IRANIL','IRA, NILAN','DIRECT','1'),\n" +
                " ('285','MARART','MARTINEZ, ARTHURO','DIRECT','1'),\n" +
                " ('286','JACRON','RONIE,JACUBA','DIRECT','1'),\n" +
                " ('287','YAN,REN','RENE YANONG','DIRECT','1'),\n" +
                " ('288','PRIPRE','PRIMERA FRESCA','DIRECT','1'),\n" +
                " ('289','PRIFRE','FRESCA,PRIMERE','DIRECT','1'),\n" +
                " ('290','S&R','S&R MEMBERSHIP SHOPPING MALL','DIRECT','1'),\n" +
                " ('291','ESRMAR','ESR MARKET MARKET','DIRECT','1'),\n" +
                " ('292','ESR','ESRTAGUIG','DIRECT','1'),\n" +
                " ('293','MAREVA','MARCOS, EVANGELINE','DIRECT','1'),\n" +
                " ('294','BADEDW','BADIAO, EDWIN','DIRECT','1'),\n" +
                " ('295','CANJAS','JASON CANO','DIRECT','1'),\n" +
                " ('296','MAAEVE','MAAGHOP, EVELYN','DIRECT','1'),\n" +
                " ('297','WILSEA','WILD SEAFOODS','DIRECT','1'),\n" +
                " ('298','FREPRI','FRESCA, PRIMERA','DIRECT','1'),\n" +
                " ('299','LIMHEL','LIM, HELEN','DIRECT','1'),\n" +
                " ('300','PRIFREFT','PRIMERE FRESCA','DIRECT','1'),\n" +
                " ('301','FERPAU','FERIA, PAULA','DIRECT','1'),\n" +
                " ('302','CAMRON','RONALDO CAMPO','DIRECT','1'),\n" +
                " ('303','MIKFIS','MIKISA FISH TRADING','DIRECT','1'),\n" +
                " ('304','GREEN','GREENHILLS SUNDAY MARKET','DIRECT','1'),\n" +
                " ('305','ORICOL','ORIENT COLD','DIRECT','1'),\n" +
                " ('306','ALVNOR','ALVENDIA, NORA','DIRECT','1'),\n" +
                " ('307','AQURON','AQUINO, RONIE','DIRECT','1'),\n" +
                " ('308','AGUMAR','MARC AGUIMATANG','DIRECT','1'),\n" +
                " ('309','ONGKAR','ONG, CARINA','DIRECT','1'),\n" +
                " ('310','DELMAU','DELA CRUZ, MAUREEN','DIRECT','1'),\n" +
                " ('311','ZORRIC','ZORCA, RICKY','DIRECT','1'),\n" +
                " ('312','UTOWIL','WILSON OTONG','DIRECT','1'),\n" +
                " ('313','ESTMIC','ESTEVEZ, MICHAEL','DIRECT','1'),\n" +
                " ('314','PANMAR','PANAHON, MARIVIC','DIRECT','1'),\n" +
                " ('315','MERTAG','MERCATO TAGUIG','DIRECT','1'),\n" +
                " ('316','OCSI','OCSI','DIRECT','1'),\n" +
                " ('317','CENANA','CENTENO, ANA','DIRECT','1'),\n" +
                " ('318','DELOSMC','DELOS REYES MC','DIRECT','1'),\n" +
                " ('319','DUMBEN','DUMAQUITA, BENJAMIN','DIRECT','1'),\n" +
                " ('320','MYOWN','MY OWN MEAT SHOP','DIRECT','1'),\n" +
                " ('321','SAMSON','SAMSON, SONNY','DIRECT','1'),\n" +
                " ('322','TANLIN','TAN, LINA','DIRECT','1'),\n" +
                " ('323','MANJEN','MANIEGO, JENNY','DIRECT','1'),\n" +
                " ('324','TIOMER','MERLENE TIO','DIRECT','1'),\n" +
                " ('325','PLAARI','PLARISAN, ARIEL','DIRECT','1'),\n" +
                " ('326','BOLBAY','BOLO (BAYAYA)','DIRECT','1'),\n" +
                " ('327','CNS','CN SALAMAT TRADING CO.','DIRECT','1'),\n" +
                " ('328','LUZBAY','LUZ BAYAYA','DIRECT','1'),\n" +
                " ('329','SGSANT','SG. SANTOS','DIRECT','1'),\n" +
                " ('330','DAMLOR','DAMASCO, LAURENE','DIRECT','1'),\n" +
                " ('331','LARKRI','LAGRIMAS, KRISTAL','DIRECT','1'),\n" +
                " ('332','DESCAN','DESSIREE CANLAS','DIRECT','1'),\n" +
                " ('333','DIGRUB','DIGNADICE, RUBEN','DIRECT','1'),\n" +
                " ('334','PENRAN','PE�AFLORIDA, RANIEL','DIRECT','1'),\n" +
                " ('335','CHUBAB','CHUA, BABY','DIRECT','1'),\n" +
                " ('336','QUICYN','QUIAMBAO, CYNTHIA','DIRECT','1'),\n" +
                " ('337','CARJUN','CARIO, JUNNEL','DIRECT','1'),\n" +
                " ('338','MISBAY','MISIBIS BAY','DIRECT','1'),\n" +
                " ('339','MRSCAS','MRS. CASTRO','DIRECT','1'),\n" +
                " ('340','PRIFOR','PRIMERA FRESCA (SNR FORT)','DIRECT','1'),\n" +
                " ('341','PRIMUN','PRIMERA FRESCA (SNR MU�OZ)','DIRECT','1'),\n" +
                " ('342','PRIQI','PRIMERA FRESCA (PG QI)','DIRECT','1'),\n" +
                " ('343','ABAKAT','ABAYA KAT','DIRECT','1'),\n" +
                " ('344','CABMAR','CABLAYAN, MARIAN','DIRECT','1'),\n" +
                " ('345','MURHUB','MURILLO, HUBERT','DIRECT','1'),\n" +
                " ('346','BHOMAN','BHONG, MANNY','DIRECT','1'),\n" +
                " ('347','PUACLA','PUA, CLARISS','DIRECT','1'),\n" +
                " ('348','TANGGI','TANG, GIZA','DIRECT','1'),\n" +
                " ('349','DUMAL','DUMAPLIN, ALFRED','DIRECT','1'),\n" +
                " ('350','SANART','SANTOS, ART','DIRECT','1'),\n" +
                " ('351','SHARES','SHABUYAKI RESTAURANT','DIRECT','1'),\n" +
                " ('352','SONROX','SONNY ROXAS (ST.JOSEPH)','DIRECT','1'),\n" +
                " ('353','TIGPRO','TIGER PROPERTY DEVELOPERS GRP. INC.','DIRECT','1'),\n" +
                " ('354','ROLLCA','SG ROLLY CAASI','DIRECT','1'),\n" +
                " ('355','PERDOL','PERIALES DOLORES','DIRECT','1'),\n" +
                " ('356','SGCAA','SGUARD CAASI','DIRECT','1'),\n" +
                " ('357','ALEMAN','ALEXANDER MENDOZA','DIRECT','1'),\n" +
                " ('358','ALIBON','ALICE BONOAN','DIRECT','1'),\n" +
                " ('359','DINNAZ','DINO NAZARREA','DIRECT','1'),\n" +
                " ('360','ARIREP','ARIEL ANDAYA','DIRECT','1'),\n" +
                " ('361','HARPLA','HARIZON PLAZA','DIRECT','1'),\n" +
                " ('362','MAAMCH','MAAM CHI','DIRECT','1'),\n" +
                " ('363','REYANG','REYNALDO ANGELES','DIRECT','1'),\n" +
                " ('364','AGCOT','AG COTCHING','DIRECT','1'),\n" +
                " ('365','JAYSAG','JAYSON AGUSTIN','DIRECT','1'),\n" +
                " ('366','PETCAR','PETER CARIAGA OCSI','DIRECT','1'),\n" +
                " ('367','JEFNOG','JEFF NOGAS DICER N.E.','DIRECT','1'),\n" +
                " ('368','PNPBAY','PNP BAYAYA','DIRECT','1'),\n" +
                " ('369','XANDER','ALEXANDER BAUTISTA','DIRECT','1'),\n" +
                " ('370','ALBERT','MISTER ALBERTO','DIRECT','1'),\n" +
                " ('371','KRING','LINA KRING GANDO','DIRECT','1'),\n" +
                " ('372','MONZUR','PO2MONZURA','DIRECT','1'),\n" +
                " ('373','RIVER','RIVERBROOK TRADING C/O ZALDY','DIRECT','1'),\n" +
                " ('374','BALBRE','BALLUCANAG, BRENDA','DIRECT','1'),\n" +
                " ('375','AILEEN','AILEEN SERRANO','DIRECT','1'),\n" +
                " ('376','ISLAND','ISLAND COVE','DIRECT','1'),\n" +
                " ('377','ROSESA','ROSE SANTOS','DIRECT','1'),\n" +
                " ('378','WENDY','WENDY KENG','DIRECT','1'),\n" +
                " ('379','ALBERTO','ALBERT DE PAZ','DIRECT','1'),\n" +
                " ('380','MAROL','MAROL CABOA','DIRECT','1'),\n" +
                " ('381','ONGCLA','ONG, CLARICE','DIRECT','1'),\n" +
                " ('382','ONGLOR','ONG, LAWRENCE','DIRECT','1'),\n" +
                " ('383','TANSCO','TAN, SCOTT','DIRECT','1'),\n" +
                " ('384','ALICE','ALICE YU','DIRECT','1'),\n" +
                " ('385','MARIE','MARIE MEDRIANO','DIRECT','1'),\n" +
                " ('386','LOMBOY','COUNCILOR LOMBOY','DIRECT','1'),\n" +
                " ('387','MADZ','MADZ ALAWIN','DIRECT','1'),\n" +
                " ('388','MARE','MARE CORPORATION','DIRECT','1'),\n" +
                " ('389','XTIAN','CHRISTIAN SANJO ABELLERA','DIRECT','1'),\n" +
                " ('390','MARIAN','MARIAN QUERUBIN HR','DIRECT','1'),\n" +
                " ('391','GAISANO','VALUE SHOP MARKET2X','DIRECT','1'),\n" +
                " ('392','HUBERT','SIR HUBERT (WEST)','DIRECT','1'),\n" +
                " ('393','JESS','JESS AQUA MARINE TRADERS INC','DIRECT','1'),\n" +
                " ('394','JOSIE','JOSIE BOLO','DIRECT','1'),\n" +
                " ('395','MERLEEN','MERLEEN TIU','DIRECT','1'),\n" +
                " ('396','RUTH','MAM RUTH (WEST)','DIRECT','1'),\n" +
                " ('397','TOMO','KGWD RUEL TOMO','DIRECT','1'),\n" +
                " ('398','SAINT','ST. JOSEPH FISH BROKERAGE INC','DIRECT','1'),\n" +
                " ('399','NOVA','PUREGOLD NOVALICHES','DIRECT','1'),\n" +
                " ('400','DOCTOR','DRA. PANGILINAN','DIRECT','1'),\n" +
                " ('401','CHING','CHING ABRIL','DIRECT','1'),\n" +
                " ('402','GARBAG','JOHN ORVETA','DIRECT','1'),\n" +
                " ('403','LIZASY','LIZA SY','DIRECT','1'),\n" +
                " ('404','WILSON','WILSON T. OTONG','DIRECT','1'),\n" +
                " ('405','IRENE','IRENE MIGUEL','DIRECT','1'),\n" +
                " ('406','RDEX','RDEX FOOD INTERNATIONAL PHIL, INC','DIRECT','1'),\n" +
                " ('407','JUDY','MAM JUDY (WEST TRADE)','DIRECT','1'),\n" +
                " ('408','CERLIO','CERBITO, LIONEL','DIRECT','1'),\n" +
                " ('409','CUTANDA','RIZALDY CUTANDA (DISER)','DIRECT','1'),\n" +
                " ('410','PGPIO','PUREGOLD PIONEER','DIRECT','1'),\n" +
                " ('411','VISAYAS','PUREGOLD VISAYAS','DIRECT','1'),\n" +
                " ('412','GARCOL','GARBAGE COLLECTOR','DIRECT','1'),\n" +
                " ('413','MARVS','MARVIN CHAVEZ','DIRECT','1'),\n" +
                " ('414','COLEA','LEAH CO','DIRECT','1'),\n" +
                " ('415','JOSE','JOSEPHINE TARAYO','DIRECT','1'),\n" +
                " ('416','KATTAN','KATHERINE TAN','DIRECT','1'),\n" +
                " ('417','DAPH','DAPH CABAUATAN','DIRECT','1'),\n" +
                " ('418','GUINTO','MARNIE GUINTO','DIRECT','1'),\n" +
                " ('419','RACEL','MYLENE RACELIS','DIRECT','1'),\n" +
                " ('420','SHELL','SHIELA LOPEZ','DIRECT','1'),\n" +
                " ('421','VILLA','JINENA VILLA','DIRECT','1'),\n" +
                " ('422','EDRA','EDGAR RAGOS','DIRECT','1'),\n" +
                " ('423','MESA','PUREGOLD STA.MESA','DIRECT','1'),\n" +
                " ('424','ABERHE','ABELLA, RHEA','DIRECT','1'),\n" +
                " ('425','DELNEN','DELA CRUZ, NENITA','DIRECT','1'),\n" +
                " ('426','LUBJOY','LUBRICO, JOY','DIRECT','1'),\n" +
                " ('427','MAGJEF','MAGTOTO, JEFF','DIRECT','1'),\n" +
                " ('428','MONLIZ','MONTANO, LIZA','DIRECT','1'),\n" +
                " ('429','NIEABE','NIEVES, ABELA','DIRECT','1'),\n" +
                " ('430','PULPAT','PULUMBARIT, PATRICK','DIRECT','1'),\n" +
                " ('431','SALMON','SALTO, MONTI','DIRECT','1'),\n" +
                " ('432','TABCAT','TABUNDA, CATALINA','DIRECT','1'),\n" +
                " ('433','BELLIZ','BELIGARIO, LIZA','DIRECT','1'),\n" +
                " ('434','FELRIN','FELISSIMO, RINA','DIRECT','1'),\n" +
                " ('435','ORIARL','ORIAS, ARLENE','DIRECT','1'),\n" +
                " ('436','FTCRUZ','FT CRUZ-SUGPO CONSIGNACION','DIRECT','1'),\n" +
                " ('437','ONGMAR','ONG, MARNIE','DIRECT','1'),\n" +
                " ('438','LUGCAF','LUGANG CAFE','DIRECT','1'),\n" +
                " ('439','PGCAL','PG CALICANTO','DIRECT','1'),\n" +
                " ('440','PGCOLA','PG COLAGO','DIRECT','1'),\n" +
                " ('441','PGSAPA','PG SAN PABLO HI WAY','DIRECT','1'),\n" +
                " ('442','MSDY','MISS DY','DIRECT','1'),\n" +
                " ('443','BOLMAR','BOLANOS, MARJORIE','DIRECT','1'),\n" +
                " ('444','RAMNIM','RAMON NIMES','DIRECT','1'),\n" +
                " ('445','HELTON','HELEN TONG','DIRECT','1'),\n" +
                " ('446','JOSCHA','JOSIE CHA','DIRECT','1'),\n" +
                " ('447','LINSY','LINDA SY','DIRECT','1'),\n" +
                " ('448','PAUCHA','PAULENE CHAN','DIRECT','1'),\n" +
                " ('449','PGBAL','PG-BALINTAWAK','DIRECT','1'),\n" +
                " ('450','REYREB','REYES, REBECCA','DIRECT','1'),\n" +
                " ('451','SANDEL','SAN JOSE DEL MONTE','DIRECT','1'),\n" +
                " ('452','ANGJUL','ANG, JULIE','DIRECT','1'),\n" +
                " ('453','RAZJOS','RAZ, JOSEPH','DIRECT','1'),\n" +
                " ('454','PGBALI','PG BALINTAWAK','DIRECT','1'),\n" +
                " ('455','JADMAR','JADOL-ON MARCIAL','DIRECT','1'),\n" +
                " ('456','BONBAL','BONIFACIA BALIONG','DIRECT','1'),\n" +
                " ('457','JANELE','JANDHE ELEGINO','DIRECT','1'),\n" +
                " ('458','MAYOFF','MAYOR''S OFFICE','DIRECT','1'),\n" +
                " ('459','ESCROB','ESCOTO, ROBERTO','DIRECT','1'),\n" +
                " ('460','AGRILI','AGRILINK','DIRECT','1'),\n" +
                " ('461','CYNVIL','CYNTHIA P. VILLARUZ','DIRECT','1'),\n" +
                " ('462','SGUSMA','SG. HENRY USMAN','DIRECT','1'),\n" +
                " ('463','MARKET','MARKET MARKET','DIRECT','1'),\n" +
                " ('464','JONSAW','JONI SAW','DIRECT','1'),\n" +
                " ('465','BOYGAL','BOYET GALANG','DIRECT','1'),\n" +
                " ('466','PAULMY','PAUL MICHAEL YMALAY','DIRECT','1'),\n" +
                " ('467','FREYAP','FRED YAP','DIRECT','1'),\n" +
                " ('468','NINRES','NINAK RESTAURANT','DIRECT','1'),\n" +
                " ('469','OCIGER','OCIER, GERALDINE','DIRECT','1'),\n" +
                " ('470','GIPALP','GIPAYA, ALPHA','DIRECT','1'),\n" +
                " ('471','MERRIC','MERCADO, RICARDO','DIRECT','1'),\n" +
                " ('472','NOGJEF','NOGA, JEFFREY','DIRECT','1'),\n" +
                " ('473','METGAI','METRO GAISANO-ANONAS','DIRECT','1'),\n" +
                " ('474','ALEXBA','ALEXANDER BAUSTISTA','DIRECT','1'),\n" +
                " ('475','DIONYG','DIONY GERANGAYA','DIRECT','1'),\n" +
                " ('476','FAUSTA','MALOLOS','DIRECT','1'),\n" +
                " ('477','MALGRCLND','MALOLOS GRACELAND','DIRECT','1'),\n" +
                " ('478','MGTAGUIG','MTERO GAISANO TAGUIG','DIRECT','1'),\n" +
                " ('479','PGCALU','PG CALUAN','DIRECT','1'),\n" +
                " ('480','LAWONG','LAURENCE ONG','DIRECT','1'),\n" +
                " ('481','CHRISGO','CHRIS GO','DIRECT','1'),\n" +
                " ('482','MARYYU','MARYYU','DIRECT','1'),\n" +
                " ('483','LALSOR','LALAINE SORIANO','DIRECT','1'),\n" +
                " ('484','LUCLEE','LUCY LEE','DIRECT','1'),\n" +
                " ('485','BABMEN','BABY MENDOZA','DIRECT','1'),\n" +
                " ('486','LLATIU','LLANNE TIU','DIRECT','1'),\n" +
                " ('487','JUDONG','JUDY ONG','DIRECT','1'),\n" +
                " ('488','MIRLGO','MIRLEY GO','DIRECT','1'),\n" +
                " ('489','MIMGUE','MIMI GUE','DIRECT','1'),\n" +
                " ('490','LINTIU','LINDA TIU','DIRECT','1'),\n" +
                " ('491','NANHER','NANCY HERRINS','DIRECT','1'),\n" +
                " ('492','MAIQUA','MAIZZE QUA','DIRECT','1'),\n" +
                " ('493','ALBLIP','ALBERTA LIPOCO','DIRECT','1'),\n" +
                " ('494','ANABRO','ANABEL BROWN','DIRECT','1'),\n" +
                " ('495','TARSOR','TARA SORIANO','DIRECT','1'),\n" +
                " ('496','WILTAN','WILLIAM TAN 2','DIRECT','1'),\n" +
                " ('497','LINTAN','LINDA TAN','DIRECT','1'),\n" +
                " ('498','ISAEST','ISABEL ESTRELLA','DIRECT','1'),\n" +
                " ('499','MGALAB','METRO GAISANO ALABANG','DIRECT','1'),\n" +
                " ('500','MGNEWP','METRO GAISANO NEW PORT','DIRECT','1'),\n" +
                " ('501','METALA','METRO ALABANG','DIRECT','1'),\n" +
                " ('502','NEWPOR','NEW PORT METRO GAISANO','DIRECT','1'),\n" +
                " ('503','GERJUN','GERANGAYA, JUNIE','DIRECT','1'),\n" +
                " ('504','TON''S','TONY''S SEAFOOD MARKET','DIRECT','1'),\n" +
                " ('505','NEWPORT','NEW PORT M.GAISANO','DIRECT','1'),\n" +
                " ('506','METRAL','METRO  ALABANG','DIRECT','1'),\n" +
                " ('507','BINOND','BINONDO, METRO GAISANO','DIRECT','1'),\n" +
                " ('508','OCACRI','OCAMPO, CRISEL','DIRECT','1'),\n" +
                " ('509','PASCOR','PASCASIO, CORNELIO','DIRECT','1'),\n" +
                " ('510','TORLAU','TORRES, LAURA','DIRECT','1'),\n" +
                " ('511','LOBOLE','LOBO, LESLY','DIRECT','1'),\n" +
                " ('512','SHELIL','SHERBA, LILY','DIRECT','1'),\n" +
                " ('513','GALRHI','GALANG, RHIA','DIRECT','1'),\n" +
                " ('514','DEGUZT','DE GUZMAN, TINA','DIRECT','1'),\n" +
                " ('515','SINALV','SINSIOCO, ALVIN','DIRECT','1'),\n" +
                " ('516','CRUZAL','CRUZ, ANGELO','DIRECT','1'),\n" +
                " ('517','MARMAT','MARTINEZ, MATHEW','DIRECT','1'),\n" +
                " ('518','MASROS','MASENDO, ROSE','DIRECT','1'),\n" +
                " ('519','SUDJER','SUDLA, JERILYN','DIRECT','1'),\n" +
                " ('520','MONROS','MONTA�O, ROSEMARIE','DIRECT','1'),\n" +
                " ('521','PULRUT','PULUMBARIT, RUTH','DIRECT','1'),\n" +
                " ('522','DIOGER','DIONY G. GERANGAYA','DIRECT','1'),\n" +
                " ('523','ALAMET','METRO ALABANG.','DIRECT','1'),\n" +
                " ('524','BINOMG','BINONDO METRO GAISANO','DIRECT','1'),\n" +
                " ('525','MR.POON','MR.POON RESTAURANT','DIRECT','1'),\n" +
                " ('526','GOLDCO','GOLD COAST SEAFOOD','DIRECT','1'),\n" +
                " ('527','METGAIAN','METRO GAISANO ANGELES','DIRECT','1'),\n" +
                " ('528','GOLBAY','GOLDEN BAY SEAFOOD','DIRECT','1'),\n" +
                " ('529','EDMTOB','EDMOND TOBIAS','DIRECT','1'),\n" +
                " ('530','GOLCOA','GOLD COAST SEAFOODS','DIRECT','1'),\n" +
                " ('531','JOACRU','JOAN CRUZ','DIRECT','1'),\n" +
                " ('532','SANCOP','SANTEH COOP II','DIRECT','1'),\n" +
                " ('533','LUMCHR','CHRISTIAN CHEK LUMBRE','DIRECT','1'),\n" +
                " ('534','LACMAR','MARICEL LACDAO','DIRECT','1'),\n" +
                " ('535','CHRLUM','CHRISTIAN LUMBRE','DIRECT','1'),\n" +
                " ('536','VELDAN','VELARDE, DANILO','DIRECT','1'),\n" +
                " ('537','RSCFOR','RSC-FORUM','DIRECT','1'),\n" +
                " ('538','SALBHE','BHENG SALUD','DIRECT','1'),\n" +
                " ('539','SUPCAT','SUPERB CATCH INC.','DIRECT','1'),\n" +
                " ('540','FEEPRO','PRONATURAL CORPORATION','DIRECT','1'),\n" +
                " ('541','CLARIC','CLARICON MARKETING INC.','DIRECT','1'),\n" +
                " ('542','CLALAA','LAARNI CLARIN','DIRECT','1'),\n" +
                " ('543','LUGJAM','JAMINA LAURIE LUGTU','DIRECT','1'),\n" +
                " ('544','COTANN','ANNA GINA COTCHING','DIRECT','1'),\n" +
                " ('545','MACJEN','JENNA MACASAQUIT','DIRECT','1'),\n" +
                " ('546','PASLUN','LUNINGNING PASCUAL','DIRECT','1'),\n" +
                " ('547','REYCRI','CRISTINE DELOS REYES','DIRECT','1'),\n" +
                " ('548','PASJUN','JUN PASCASIO','DIRECT','1'),\n" +
                " ('549','ROSMON','ROSE MONTANO','DIRECT','1'),\n" +
                " ('550','IBAFRE','FREDDIE IBANES','DIRECT','1'),\n" +
                " ('551','LEGJUN','JUN LEGASPI','DIRECT','1'),\n" +
                " ('552','ONGLIN','LINA ONG','DIRECT','1'),\n" +
                " ('553','MARTIN','MARC & TIN TRADING','DIRECT','1'),\n" +
                " ('554','GERBEA','BEA GERONIMO','DIRECT','1'),\n" +
                " ('555','BENWES','BENG PEDRO','DIRECT','1'),\n" +
                " ('556','VILETE','ETERNAL VILLAFLOR','DIRECT','1'),\n" +
                " ('557','ONGJOR','JORGE ONG','DIRECT','1'),\n" +
                " ('558','SANNER','NERRIZA SAN ANDRES','DIRECT','1'),\n" +
                " ('559','JMO','JUNE ORDO�O','DIRECT','1'),\n" +
                " ('560','BLUCOO','BLUEWATER SERVICE COOPERATIVE','DIRECT','1'),\n" +
                " ('561','RSCPROFR','RSC - PROMO FREEBIES','DIRECT','1'),\n" +
                " ('562','MERBOY','BOY MERCADO','DIRECT','1'),\n" +
                " ('563','RICBUN','RICE BUNNIES','DIRECT','1'),\n" +
                " ('564','MARC&TIN','MARC AND TIN TRADING','DIRECT','1'),\n" +
                " ('565','METRET','METRO RETAIL STORES GROUP, INC.','DIRECT','1'),\n" +
                " ('566','LAJE','JENINO LACDAO','DIRECT','1'),\n" +
                " ('567','HTCPRE','HTCG PREMIUM FOOD CONCEPT INC.','DIRECT','1'),\n" +
                " ('568','ALVIN','JUNE MEDRIANO ORDONO','DIRECT','1'),\n" +
                " ('569','MAGNEO','NEONORA MAGAT','DIRECT','1'),\n" +
                " ('570','OGIONG','OGIE ONG','DIRECT','1'),\n" +
                " ('571','FORYCA','FORTUNATA YCASAS','DIRECT','1'),\n" +
                " ('572','JESCAH','JESSIE CAHILIG','DIRECT','1'),\n" +
                " ('573','RONSOR','RONALD SORIANO','DIRECT','1'),\n" +
                " ('574','DONREG','DONNA REGALIA','DIRECT','1'),\n" +
                " ('575','BANLEAF','BANANA LEAFS','DIRECT','1'),\n" +
                " ('576','COTMOI','MOISES COTCHING','DIRECT','1'),\n" +
                " ('577','DACJES','JESUS DACUBA','DIRECT','1'),\n" +
                " ('578','DEGJOE','JOENAS DE GUZMAN','DIRECT','1'),\n" +
                " ('579','YEAWIL','WILSON YEAN','DIRECT','1'),\n" +
                " ('580','CALROS','ROSELYN CALVAN','DIRECT','1'),\n" +
                " ('581','TABALB','ALBERTO TABAYOYONG','DIRECT','1'),\n" +
                " ('582','DELREYCR','CRISTINE DE LOS REYES','DIRECT','1'),\n" +
                " ('583','CRUDAR','DARWIN CRUZ','DIRECT','1'),\n" +
                " ('584','TORDOL','DOLLY TORRES','DIRECT','1'),\n" +
                " ('585','EJOELL','ELLEN EJOC','DIRECT','1'),\n" +
                " ('586','GUIGEN','GENESIS GUINTO','DIRECT','1'),\n" +
                " ('587','SECSAB','SABAS SECUYA','DIRECT','1'),\n" +
                " ('588','VINGELFA','MR. VINCENT GEL FAJARILLO','DIRECT','1'),\n" +
                " ('589','PABJOS','JOSEPH PABLO','DIRECT','1'),\n" +
                " ('590','FWMEASHO','FW MEAT SHOP','DIRECT','1'),\n" +
                " ('591','PRK','RKS','DIRECT','1'),\n" +
                " ('592','TANAIL','AILEEN TAN','DIRECT','1'),\n" +
                " ('593','TANALB','ALBERTA ONG','DIRECT','1'),\n" +
                " ('594','MSGINA','MS. GINA','DIRECT','1'),\n" +
                " ('595','YUPANG','MR.  ROBERT YUPANGCO','DIRECT','1'),\n" +
                " ('596','JERYAP','JEREMY YAP','DIRECT','1'),\n" +
                " ('597','ANGDIA','DIA ANG','DIRECT','1'),\n" +
                " ('598','PHIONG','PHILIP ONG','DIRECT','1'),\n" +
                " ('599','SIAROM','ROMY SIA','DIRECT','1'),\n" +
                " ('600','TANCHR','CHRISTIAN TAN','DIRECT','1'),\n" +
                " ('601','ONGLES','LESLIE ONG','DIRECT','1'),\n" +
                " ('602','CFY','CFY FOOD SOURCE CORP.','DIRECT','1'),\n" +
                " ('603','TYJAM','JAMES TY','DIRECT','1'),\n" +
                " ('604','TUILIA','GREEN HILLS','DIRECT','1'),\n" +
                " ('605','ONGNEL','NELSON ONG','DIRECT','1'),\n" +
                " ('606','JUDONG','JUDY ONG','DIRECT','1'),\n" +
                " ('607','UYJUL','UY, JULIE','DIRECT','1'),\n" +
                " ('608','DR','DR.BONOAN','DIRECT','1'),\n" +
                " ('609','TONLUL','LULU TONG','DIRECT','1'),\n" +
                " ('610','GALFAI','FAITH GALENDEZ','DIRECT','1'),\n" +
                " ('611','JMDSEA','JMD SEAFOODS DEALER','DIRECT','1'),\n" +
                " ('612','FIRCLAFO','FIRST CLASS FOOD CONCEPT INC.','DIRECT','1'),\n" +
                " ('613','GARLOU','LOUIE GARCIA','DIRECT','1'),\n" +
                " ('614','KATSUR','KATSURAMEN, INC TAMPOPO BHS','DIRECT','1'),\n" +
                " ('615','TIUKAT','KATRINE TIU','DIRECT','1'),\n" +
                " ('616','LOPJUL','JULIET LOPEZ','DIRECT','1'),\n" +
                " ('617','GARLUC','LUCK GARDEN SEAFOOD TEA HOUSE','DIRECT','1'),\n" +
                " ('618','TANGRA','GRACE TAN','DIRECT','1'),\n" +
                " ('619','PIA','PIA','DIRECT','1'),\n" +
                " ('620','SIBJEN','JENNY SIBAYAN','DIRECT','1'),\n" +
                " ('621','BONCHI','SCOTTLAND INC.','DIRECT','1'),\n" +
                " ('622','BARJOY','JOY BARIERO','DIRECT','1'),\n" +
                " ('623','SAMLAL','SAMUEL G. LALOSA, JR','DIRECT','1'),\n" +
                " ('624','JOSDEN','JOSEPH DENAJEBA','DIRECT','1'),\n" +
                " ('625','TANSTA','STAN TANCHAN','DIRECT','1'),\n" +
                " ('626','IFEX','IFEX','DIRECT','1'),\n" +
                " ('627','WILDSEA','WILD SEAFOOD','DIRECT','1'),\n" +
                " ('628','CHOCHO','CHOOBI CHOOBI FLAVOR CORP','DIRECT','1'),\n" +
                " ('629','DANCAB','DANIEL CABRERA','DIRECT','1'),\n" +
                " ('630','UNICAF','UNIVERSITY CAFETERIA','DIRECT','1'),\n" +
                " ('631','RICLIN','RICHLINE GENERAL MERCHANDISE INC.','DIRECT','1'),\n" +
                " ('632','CONQUE','CONRAD QUE','DIRECT','1'),\n" +
                " ('633','DELCAR','CAROLINE DEL ROSARIO','DIRECT','1'),\n" +
                " ('634','TOUSTO','TOUCH STONE','DIRECT','1'),\n" +
                " ('635','TEPJES','TEPAIT JESSIE','DIRECT','1'),\n" +
                " ('636','SCOTINC','SCOTTLAND, INC.','DIRECT','1'),\n" +
                " ('637','PERALE','ALEXANDER S. PERALTA','DIRECT','1'),\n" +
                " ('638','EVAMAR','EVANGELINA MARCOS','DIRECT','1'),\n" +
                " ('639','SIRDOLI','ALAS DALMACIO','DIRECT','1'),\n" +
                " ('640','GESPLA','GESELA PLATON','DIRECT','1'),\n" +
                " ('641','ROSLAU','ROSE LAURIO','DIRECT','1'),\n" +
                " ('642','GRABAL','GRACE BALLERA','DIRECT','1'),\n" +
                " ('643','CATABA','CATHERINE ABAYA','DIRECT','1'),\n" +
                " ('644','ESPJUD','JUDY ESPIRITO','DIRECT','1'),\n" +
                " ('645','JEBELS','ELSA JEBULAN','DIRECT','1'),\n" +
                " ('646','NESRIC','NESORTADO, RICHARD','DIRECT','1'),\n" +
                " ('647','PGSANPABHI','PUREGOLD PRICE CLUB INC-SAN PABLO HI WAY','DIRECT','1'),\n" +
                " ('648','PGSANPABSA','PUREGOLD PRICE CLUB INC-SAN PABLO SAMBAT','DIRECT','1'),\n" +
                " ('649','PGPAGSAN','PUREGOLD PRICE CLUB INC - PAGSANJAN','DIRECT','1'),\n" +
                " ('650','PGCALA','PUREGOLD PRICE CLUB INC - CALAMBA CROSSING','DIRECT','1'),\n" +
                " ('651','PGTAGA','PUREGOLD PRICE CLUB INC - TAGAPO','DIRECT','1'),\n" +
                " ('652','PGCALAUAN','PUREGOLD PRICE CLUB INC - CALAUAN','DIRECT','1'),\n" +
                " ('653','PGCAMCT','PUREGOLD PRICE CLUB INC - CALAMBA CROSSTOWN','DIRECT','1'),\n" +
                " ('654','PGCAHAL','PUREGOLD PRICE CLUB INC - CALAMBA HALANG','DIRECT','1'),\n" +
                " ('655','PGJRCRO','PUREGOLD JUNIOR SUPERMARKET INC. - CROSSTOWN','DIRECT','1'),\n" +
                " ('656','ANGREY','ANGELES. REYNALDO','DIRECT','1'),\n" +
                " ('657','CUTDAR','DARIO CUTANDA','DIRECT','1'),\n" +
                " ('658','MATANN','MATEO, ANNE','DIRECT','1'),\n" +
                " ('659','DEVERA','EDUARDO DE VERA','DIRECT','1'),\n" +
                " ('660','DUQBEN','BENIGNO DUQUE','DIRECT','1'),\n" +
                " ('661','NERERI','ERIC NERPIO','DIRECT','1'),\n" +
                " ('662','SALSHA','SALIANON SHAYNE MARISSA','DIRECT','1'),\n" +
                " ('663','TANGLI','LINDA TANG','DIRECT','1'),\n" +
                " ('664','HENJUN','JUN HEN','DIRECT','1'),\n" +
                " ('665','BIZU','PHILIPPINE PASTRIES INC. (BIZU)','DIRECT','1'),\n" +
                " ('666','WCL','WCL COLD STORAGE SOLUTIONS, INC.','DIRECT','1'),\n" +
                " ('667','JULLOP','JULIETA LOPEZ','DIRECT','1'),\n" +
                " ('668','LUCGAR','LUCK GARDEN','DIRECT','1'),\n" +
                " ('669','PADJUM','JUM PADERES','DIRECT','1'),\n" +
                " ('670','SABCES','SABIT, CESAR','DIRECT','1'),\n" +
                " ('671','TIUHAP','TIU, HAPPY','DIRECT','1'),\n" +
                " ('672','TIUEVI','TIU, EVIE','DIRECT','1'),\n" +
                " ('673','LEGJUA','LEGASPI, JUANITO','DIRECT','1'),\n" +
                " ('674','GALSTE','GALVAN, STEPHANIE','DIRECT','1'),\n" +
                " ('675','ABALEA','ABARQUEZ, LEA','DIRECT','1'),\n" +
                " ('676','MADZCH','MADERA, ZCHAILANE','DIRECT','1'),\n" +
                " ('677','BERMHA','BERNABE, MHAI','DIRECT','1'),\n" +
                " ('678','TANDAISI','TANGI, DAISIBEL','DIRECT','1'),\n" +
                " ('679','SOPJOE','JOEY SOPERIALES','DIRECT','1'),\n" +
                " ('680','SALMARSH','MARISSA SHAYNE SALIANON','DIRECT','1'),\n" +
                " ('681','ROMRON','RONALD ROMERO','DIRECT','1'),\n" +
                " ('682','ISMRON','RONALYN ISMAEL','DIRECT','1'),\n" +
                " ('683','CASMAG','MAGGIE CASTINO','DIRECT','1'),\n" +
                " ('684','FUEMEN','MENARD FUENTES','DIRECT','1'),\n" +
                " ('685','PANISA','PANGILINAN, MARIA ISABEL','DIRECT','1'),\n" +
                " ('686','JAY','JEREMIAH DELA REA','DIRECT','1'),\n" +
                " ('687','MAGEDY','MAGKASI, EDYRIAN','DIRECT','1'),\n" +
                " ('688','RODJUN','RODRIGUEZ, JUNNEL','DIRECT','1'),\n" +
                " ('689','RIVKRI','RIVERO, KRISTALYN','DIRECT','1'),\n" +
                " ('690','KINMAR','KING MARINE','DIRECT','1'),\n" +
                " ('691','DELLIZ','DELOS REYES, LIZA','DIRECT','1'),\n" +
                " ('692','DENCHU','DENNIS CHU','DIRECT','1'),\n" +
                " ('693','PANPAU','PAUL PANGAN','DIRECT','1'),\n" +
                " ('694','METGAIIM','METRO GAISANO IMUS','DIRECT','1'),\n" +
                " ('695','METROANT','METRO GAISANO ANTIPOLO','DIRECT','1'),\n" +
                " ('696','METROBIN','METRO GAISANO BINONDO','DIRECT','1'),\n" +
                " ('697','METROMAN','METRO GAISANO MANDALUYONG','DIRECT','1'),\n" +
                " ('698','LBC','LBC EXPRESS INC.','DIRECT','1'),\n" +
                " ('699','CHIDAM','CHIARA DAMIAO','DIRECT','1'),\n" +
                " ('700','CHRQUA','CHRISTINE QUANICO','DIRECT','1'),\n" +
                " ('701','RAMJOS','RAMOS, JOSE FELIX','DIRECT','1'),\n" +
                " ('702','SANJIM','SAN PEDRO, JIMMY','DIRECT','1'),\n" +
                " ('703','VILNEL','VILLAFLOR, NELL','DIRECT','1'),\n" +
                " ('704','WALCAR','WM CARMONA','DIRECT','1'),\n" +
                " ('705','MIGIRE','MIGUEL, IRENE','DIRECT','1'),\n" +
                " ('706','RHOBOR','RHOEL BORHILLO','DIRECT','1'),\n" +
                " ('707','PGCAN','PUREGOLD JUNIOR SUPERMARKET INC. - CANLUBANG','DIRECT','1'),\n" +
                " ('708','FEBSAR','FAB SARMIENTO','DIRECT','1'),\n" +
                " ('709','MARCAR','MARY ANNE CARION','DIRECT','1'),\n" +
                " ('710','ICOJAR','ICO, JARINA','DIRECT','1'),\n" +
                " ('711','POLINDCO','POLYLITE INDUSTRIAL CORPORATION','DIRECT','1'),\n" +
                " ('712','LIMEDI','LIM, EDDIE','DIRECT','1'),\n" +
                " ('713','PGTANA','PUREGOLD PRICE CLUB INC-TANAUAN','DIRECT','1'),\n" +
                " ('714','PGSTO','PUREGOLD PRICE CLUB INC-STO TOMAS','DIRECT','1'),\n" +
                " ('715','MADFUS','MADRID FUSION','DIRECT','1'),\n" +
                " ('716','GABJOA','GABRIENTO, JOAN','DIRECT','1'),\n" +
                " ('717','DUCMAY','DUCA, MAY ANGELLIE','DIRECT','1'),\n" +
                " ('718','MALCAT','MALENAP, CATRINA','DIRECT','1'),\n" +
                " ('719','EUGHAN','EUGENIO, HANNA','DIRECT','1'),\n" +
                " ('720','TAWED','ED TAWAGON/BARCELO CAFE, LA. CO.','DIRECT','1'),\n" +
                " ('721','AILCOY','AILEEN COYUKIAT','DIRECT','1'),\n" +
                " ('722','JESANG','JESSICA ANG','DIRECT','1'),\n" +
                " ('723','MICKIM','MICHELLE KIM','DIRECT','1'),\n" +
                " ('724','KIMMIC','KIM, MICHELLE','DIRECT','1'),\n" +
                " ('725','BANCHO','MR. BANSAN CHOA','DIRECT','1'),\n" +
                " ('726','SOUCOR','SOUTHERNSUMMIT CORPORATION','DIRECT','1'),\n" +
                " ('727','JIMCAB','JIM PAUL CABRIDO','DIRECT','1'),\n" +
                " ('728','PGJRTAG','PUREGOLD JUNIOR SUPERMARKET INC.-TAGAYTAY','DIRECT','1'),\n" +
                " ('729','PGMAR','PUREGOLD JUNIOR SUPERMARKET INC.-NANGKA(MARIK','DIRECT','1'),\n" +
                " ('730','PGJRSANMA','PUREGOLD JUNIOR SUPERMARKET INC.-SAN MATEO','DIRECT','1'),\n" +
                " ('731','PGGENTRI','PUREGOLD JUNIOR SUPERMARKET INC.-GENERAL TRIA','DIRECT','1'),\n" +
                " ('732','PGTREMAR','PUREGOLD JUNIOR CLUB INC.-TRECE MARTIRES','DIRECT','1'),\n" +
                " ('733','PGROS','PUREGOLD PRICE CLUB INC.-ROSARIO, CAVITE','DIRECT','1'),\n" +
                " ('734','PGDASHWAY','PUREGOLD PRICE CLUB INC.-DASMARINAS HIGH WAY','DIRECT','1'),\n" +
                " ('735','PGGMA','PUREGOLD PRICE CLUB INC.-GMA','DIRECT','1'),\n" +
                " ('736','PGMON','PUREGOLD PRICE CLUB INC-MONTALBAN','DIRECT','1'),\n" +
                " ('737','PGTRETO','PUREGOLD PRICE CLUB INC.-TRECE TOWER MALL','DIRECT','1'),\n" +
                " ('738','SALBEN','SALUD, BENG','DIRECT','1'),\n" +
                " ('739','PGMARI','PUREGOLD PRICE CLUB INC.,-MARILAO','DIRECT','1'),\n" +
                " ('740','PGGUI','PUREGOLD PRICE CLUB INC., GUIGUINTO','DIRECT','1'),\n" +
                " ('741','PGMAL','PUREGOLD PRICE CLUB INC.,-MALOLOS','DIRECT','1'),\n" +
                " ('742','PGHUGPE','PUREGOLD PRICE CLUB INC., - HUGO PEREZ TRECE','DIRECT','1'),\n" +
                " ('743','HANBUR','HANSBURY INCORPORATED','DIRECT','1'),\n" +
                " ('744','PGBALIUAG','PUREGOLD PRICE CLUB INC., - BALIUAG','DIRECT','1'),\n" +
                " ('745','MANUNI','MANCHESTER UNITED SCHOOL','DIRECT','1'),\n" +
                " ('746','ANGMOT','MA. ANGELA MOTON','DIRECT','1'),\n" +
                " ('747','PGCARM','PUREGOLD PRICE CLUB INC-CARMONA','DIRECT','1'),\n" +
                " ('748','QUEEDW','QUEMAN, EDWIN','DIRECT','1'),\n" +
                " ('749','LIMEDW','LIM, EDWARD','DIRECT','1'),\n" +
                " ('750','BANANA','BANANA LEAF CURRY HOUSE INC.','DIRECT','1'),\n" +
                " ('751','ESTFRE','ESTRADA, FRED','DIRECT','1'),\n" +
                " ('752','PGNOV','PUREGOLD PRICE CLUB INC-NOVELETA','DIRECT','1'),\n" +
                " ('753','SARFAB','SARMIENTO, FAB','DIRECT','1'),\n" +
                " ('754','BUEMYR','MYRA BUENA','DIRECT','1'),\n" +
                " ('755','MOTANG','ANGELA MOTON','DIRECT','1'),\n" +
                " ('756','ESTMAL','ESTANCIA MALL','DIRECT','1'),\n" +
                " ('757','CRAKAT','CRAVINGS KAT','DIRECT','1'),\n" +
                " ('758','CRAFRA','CRAVING FRASER','DIRECT','1'),\n" +
                " ('759','CRAMOL','CRAVING MOLITO','DIRECT','1'),\n" +
                " ('760','LUCIA','LUCIA','DIRECT','1'),\n" +
                " ('761','EPICUR','EPICURIOUS','DIRECT','1'),\n" +
                " ('762','C2SHAN','C2 SHANG','DIRECT','1'),\n" +
                " ('763','COMMIS','COMMISSARY','DIRECT','1'),\n" +
                " ('764','BNPILT','BNP IL TERRAZO','DIRECT','1'),\n" +
                " ('765','BNPSHA','BNP SHAW','DIRECT','1'),\n" +
                " ('766','BNPORT','BNP ORTIGAS','DIRECT','1'),\n" +
                " ('767','BNPMCK','BNP MCKINLEY','DIRECT','1'); ");

    }



}
