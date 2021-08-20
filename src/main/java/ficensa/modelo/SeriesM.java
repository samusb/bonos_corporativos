package modelo;

import java.math.BigDecimal;

/**
 *
 * @author aprivera
 */
public class SeriesM {

    //Agregar series, Series emitidas, datos de la serie y cert. en custodia
    private String SRSCOD;
    private BigDecimal SRSAIO;
    private BigDecimal SRSPLD;
    private BigDecimal SRSMBN;
    private String SRSMNE;//decimal en base de datos
    private BigDecimal SRSTNL;
    private BigDecimal SRSTLIB;
    private BigDecimal SRSDIF;
    private BigDecimal SRSTMX;
    private BigDecimal SRSTMN;
    private String SRSFDE;
    private String SRSFDV;
    private BigDecimal SRSFNM;
    private BigDecimal SRSBMP;
    private BigDecimal SRSTBV;
    private BigDecimal SRSTBC;
    private String SRSUSR;
    private String SRSFHI;
    private String SRSFRG;
    private BigDecimal SRSCCC;
    private BigDecimal SRSMON;
    private String SRSTVR;
    private String SRSICD;

    private BigDecimal BONVIG;

    //Certificado en custodia
    private String CTCCOD;
    private BigDecimal CTCCCD;
    private BigDecimal CTCAIO;
    private String CTCIDN;
    private String CTCNOM;
    private BigDecimal CTCACC;
    private String CTCFVT;
    private String CTCFIN;
    private String CTCFRG;
    private String CTCFPA;
    private String CTCFPI;
    private String CTCEXC;
    private BigDecimal CTCIMP;
    private BigDecimal CTCBNI;
    private BigDecimal CTCBNF;
    private String CTCUSR;
    private BigDecimal CTCNMB;
    //Formas numeradas
    private BigDecimal CFNM;
    private BigDecimal FNM;
    private String FNEST;
    
     private BigDecimal FNMNUM;	
    private String FNMFIN;
    private String FNMIMP;	
    private String FNMUSR;	
    private String FNMOBS;	
    private String FNMCOD;	
    private BigDecimal FNMAIO;	
    private String FNMEST;

    public BigDecimal getFNMNUM() {
        return FNMNUM;
    }

    public void setFNMNUM(BigDecimal FNMNUM) {
        this.FNMNUM = FNMNUM;
    }

    public String getFNMFIN() {
        return FNMFIN;
    }

    public void setFNMFIN(String FNMFIN) {
        this.FNMFIN = FNMFIN;
    }

    public String getFNMIMP() {
        return FNMIMP;
    }

    public void setFNMIMP(String FNMIMP) {
        this.FNMIMP = FNMIMP;
    }

    public String getFNMUSR() {
        return FNMUSR;
    }

    public void setFNMUSR(String FNMUSR) {
        this.FNMUSR = FNMUSR;
    }

    public String getFNMOBS() {
        return FNMOBS;
    }

    public void setFNMOBS(String FNMOBS) {
        this.FNMOBS = FNMOBS;
    }

    public String getFNMCOD() {
        return FNMCOD;
    }

    public void setFNMCOD(String FNMCOD) {
        this.FNMCOD = FNMCOD;
    }

    public BigDecimal getFNMAIO() {
        return FNMAIO;
    }

    public void setFNMAIO(BigDecimal FNMAIO) {
        this.FNMAIO = FNMAIO;
    }

    public String getFNMEST() {
        return FNMEST;
    }

    public void setFNMEST(String FNMEST) {
        this.FNMEST = FNMEST;
    }

    public BigDecimal getCTCNMB() {
        return CTCNMB;
    }

    public void setCTCNMB(BigDecimal CTCNMB) {
        this.CTCNMB = CTCNMB;
    }

    
    
    public String getCTCCOD() {
        return CTCCOD;
    }

    public void setCTCCOD(String CTCCOD) {
        this.CTCCOD = CTCCOD;
    }

    public BigDecimal getCTCCCD() {
        return CTCCCD;
    }

    public void setCTCCCD(BigDecimal CTCCCD) {
        this.CTCCCD = CTCCCD;
    }

    public BigDecimal getCTCAIO() {
        return CTCAIO;
    }

    public void setCTCAIO(BigDecimal CTCAIO) {
        this.CTCAIO = CTCAIO;
    }

    public String getCTCIDN() {
        return CTCIDN;
    }

    public void setCTCIDN(String CTCIDN) {
        this.CTCIDN = CTCIDN;
    }

    public String getCTCNOM() {
        return CTCNOM;
    }

    public void setCTCNOM(String CTCNOM) {
        this.CTCNOM = CTCNOM;
    }

    
    public BigDecimal getCTCACC() {
        return CTCACC;
    }

    public void setCTCACC(BigDecimal CTCACC) {
        this.CTCACC = CTCACC;
    }

    public String getCTCFVT() {
        return CTCFVT;
    }

    public void setCTCFVT(String CTCFVT) {
        this.CTCFVT = CTCFVT;
    }

    public String getCTCFIN() {
        return CTCFIN;
    }

    public void setCTCFIN(String CTCFIN) {
        this.CTCFIN = CTCFIN;
    }

    public String getCTCFRG() {
        return CTCFRG;
    }

    public void setCTCFRG(String CTCFRG) {
        this.CTCFRG = CTCFRG;
    }

    public String getCTCFPA() {
        return CTCFPA;
    }

    public void setCTCFPA(String CTCFPA) {
        this.CTCFPA = CTCFPA;
    }

    public String getCTCFPI() {
        return CTCFPI;
    }

    public void setCTCFPI(String CTCFPI) {
        this.CTCFPI = CTCFPI;
    }

    public String getCTCEXC() {
        return CTCEXC;
    }

    public void setCTCEXC(String CTCEXC) {
        this.CTCEXC = CTCEXC;
    }

    public BigDecimal getCTCIMP() {
        return CTCIMP;
    }

    public void setCTCIMP(BigDecimal CTCIMP) {
        this.CTCIMP = CTCIMP;
    }

    public BigDecimal getCTCBNI() {
        return CTCBNI;
    }

    public void setCTCBNI(BigDecimal CTCBNI) {
        this.CTCBNI = CTCBNI;
    }

    public BigDecimal getCTCBNF() {
        return CTCBNF;
    }

    public void setCTCBNF(BigDecimal CTCBNF) {
        this.CTCBNF = CTCBNF;
    }

    public String getCTCUSR() {
        return CTCUSR;
    }

    public void setCTCUSR(String CTCUSR) {
        this.CTCUSR = CTCUSR;
    }

    
    
    public BigDecimal getCFNM() {
        return CFNM;
    }

    public void setCFNM(BigDecimal CFNM) {
        this.CFNM = CFNM;
    }

    public BigDecimal getFNM() {
        return FNM;
    }

    public void setFNM(BigDecimal FNM) {
        this.FNM = FNM;
    }

    public String getFNEST() {
        return FNEST;
    }

    public void setFNEST(String FNEST) {
        this.FNEST = FNEST;
    }

    public String getSRSCOD() {
        return SRSCOD;
    }

    public void setSRSCOD(String SRSCOD) {
        this.SRSCOD = SRSCOD;
    }

    public BigDecimal getSRSAIO() {
        return SRSAIO;
    }

    public void setSRSAIO(BigDecimal SRSAIO) {
        this.SRSAIO = SRSAIO;
    }

    public BigDecimal getSRSPLD() {
        return SRSPLD;
    }

    public void setSRSPLD(BigDecimal SRSPLD) {
        this.SRSPLD = SRSPLD;
    }

    public BigDecimal getSRSMBN() {
        return SRSMBN;
    }

    public void setSRSMBN(BigDecimal SRSMBN) {
        this.SRSMBN = SRSMBN;
    }

    public String getSRSMNE() {
        return SRSMNE;
    }

    public void setSRSMNE(String SRSMNE) {
        this.SRSMNE = SRSMNE;
    }

    public BigDecimal getSRSTNL() {
        return SRSTNL;
    }

    public void setSRSTNL(BigDecimal SRSTNL) {
        this.SRSTNL = SRSTNL;
    }

    public BigDecimal getSRSTLIB() {
        return SRSTLIB;
    }

    public void setSRSTLIB(BigDecimal SRSTLIB) {
        this.SRSTLIB = SRSTLIB;
    }

    public BigDecimal getSRSDIF() {
        return SRSDIF;
    }

    public void setSRSDIF(BigDecimal SRSDIF) {
        this.SRSDIF = SRSDIF;
    }

    public BigDecimal getSRSTMX() {
        return SRSTMX;
    }

    public void setSRSTMX(BigDecimal SRSTMX) {
        this.SRSTMX = SRSTMX;
    }

    public BigDecimal getSRSTMN() {
        return SRSTMN;
    }

    public void setSRSTMN(BigDecimal SRSTMN) {
        this.SRSTMN = SRSTMN;
    }

    public String getSRSFDE() {
        return SRSFDE;
    }

    public void setSRSFDE(String SRSFDE) {
        this.SRSFDE = SRSFDE;
    }

    public String getSRSFDV() {
        return SRSFDV;
    }

    public void setSRSFDV(String SRSFDV) {
        this.SRSFDV = SRSFDV;
    }

    public BigDecimal getSRSFNM() {
        return SRSFNM;
    }

    public void setSRSFNM(BigDecimal SRSFNM) {
        this.SRSFNM = SRSFNM;
    }

    public BigDecimal getSRSBMP() {
        return SRSBMP;
    }

    public void setSRSBMP(BigDecimal SRSBMP) {
        this.SRSBMP = SRSBMP;
    }

    public BigDecimal getSRSTBV() {
        return SRSTBV;
    }

    public void setSRSTBV(BigDecimal SRSTBV) {
        this.SRSTBV = SRSTBV;
    }

    public BigDecimal getSRSTBC() {
        return SRSTBC;
    }

    public void setSRSTBC(BigDecimal SRSTBC) {
        this.SRSTBC = SRSTBC;
    }

    public String getSRSUSR() {
        return SRSUSR;
    }

    public void setSRSUSR(String SRSUSR) {
        this.SRSUSR = SRSUSR;
    }

    public String getSRSFHI() {
        return SRSFHI;
    }

    public void setSRSFHI(String SRSFHI) {
        this.SRSFHI = SRSFHI;
    }

    public String getSRSFRG() {
        return SRSFRG;
    }

    public void setSRSFRG(String SRSFRG) {
        this.SRSFRG = SRSFRG;
    }

    public BigDecimal getSRSCCC() {
        return SRSCCC;
    }

    public void setSRSCCC(BigDecimal SRSCCC) {
        this.SRSCCC = SRSCCC;
    }

    public BigDecimal getSRSMON() {
        return SRSMON;
    }

    public void setSRSMON(BigDecimal SRSMON) {
        this.SRSMON = SRSMON;
    }

    public String getSRSTVR() {
        return SRSTVR;
    }

    public void setSRSTVR(String SRSTVR) {
        this.SRSTVR = SRSTVR;
    }

    public String getSRSICD() {
        return SRSICD;
    }

    public void setSRSICD(String SRSICD) {
        this.SRSICD = SRSICD;
    }

    public BigDecimal getBONVIG() {
        return BONVIG;
    }

    public void setBONVIG(BigDecimal BONVIG) {
        this.BONVIG = BONVIG;
    }

};
