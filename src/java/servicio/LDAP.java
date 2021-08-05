/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicio;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


/**
 *
 * @author aprivera
 */
public class LDAP {
    // protected final Log logger = LogFactory.getLog(getClass());

    public static final String DISTINGUISHED_NAME = "CN=reportes,OU=Usuarios,OU=Gerencia de Informatica,OU=ficensa,DC=ficensa,DC=com,DC=hn";    //<--- Configurar. Este debera ser un usuario Active Directory que venza NUNCA
    public static final String CN = "cn";    //<--- No tocar
    public static final String MEMBER = "member";    //<--- No tocar
    public static final String MEMBER_OF = "memberOf";    //<--- No tocar
    public static final String SEARCH_BY_SAM_ACCOUNT_NAME = "(SAMAccountName={0})";    //<--- No tocar
    public static final String SEARCH_GROUP_BY_GROUP_CN = "(&(objectCategory=group)(cn={0}))";    //<--- No tocar
  
    
  
    //public static List Usuario;

    /*
     * Prepares and returns CN that can be used for AD query
     * e.g. Converts "CN=**Dev - Test Group" to "**Dev - Test Group"
     * Converts CN=**Dev - Test Group,OU=Distribution Lists,DC=DOMAIN,DC=com to "**Dev - Test Group"
     */
    public static String getCN(String cnName) {
        if (cnName != null && cnName.toUpperCase().startsWith("CN=")) {
            cnName = cnName.substring(3);
        }
        int position = cnName.indexOf(',');
        if (position == -1) {
            return cnName;
        } else {
            return cnName.substring(0, position);
        }
    }

    public static boolean isSame(String target, String candidate) {
        if (target != null && target.equalsIgnoreCase(candidate)) {
            return true;
        }
        return false;
    }

    public static boolean authenticate(String domain, String username, String password, String groupDistinguishedName) {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");    //<--- Configurar
        env.put(Context.PROVIDER_URL, "ldap://172.26.1.37:389");    //<--- Configurar
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, domain + "\\" + username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        DirContext ctx = null;
        String defaultSearchBase = "DC=ficensa,DC=com,DC=hn";    //<--- Configurar
        
        try {
            ctx = new InitialDirContext(env);

            // userName is SAMAccountName
            SearchResult sr = executeSearchSingleResult(ctx, SearchControls.SUBTREE_SCOPE, defaultSearchBase,
                    MessageFormat.format(SEARCH_BY_SAM_ACCOUNT_NAME, new Object[]{username}),
                    new String[]{DISTINGUISHED_NAME, CN, MEMBER_OF}
            );

            String groupCN = getCN(groupDistinguishedName);
            HashMap processedUserGroups = new HashMap();
            HashMap unProcessedUserGroups = new HashMap();

            // Look for and process memberOf
            Attribute memberOf = sr.getAttributes().get(MEMBER_OF);
            if (memberOf != null) {
                for (Enumeration e1 = memberOf.getAll(); e1.hasMoreElements();) {
                    String unprocessedGroupDN = e1.nextElement().toString();
                    String unprocessedGroupCN = getCN(unprocessedGroupDN);
                    // Quick check for direct membership
                    if (isSame(groupCN, unprocessedGroupCN) && isSame(groupDistinguishedName, unprocessedGroupDN)) {
                        System.out.println(username + " is authorized.");
                        return true;
                    } else {
                        unProcessedUserGroups.put(unprocessedGroupDN, unprocessedGroupCN);
                    }
                }
                if (userMemberOf(ctx, defaultSearchBase, processedUserGroups, unProcessedUserGroups, groupCN, groupDistinguishedName)) {
                    System.out.println(username + " is authorized.");
                   
                    return true;
                }
            }

            System.out.println(username + " is NOT authorized.");
            return false;
        } catch (AuthenticationException e) {
            System.out.println(username + " is NOT authenticated");
            return false;
        } catch (NamingException e) {
            System.out.println(username + " is NOT authenticated");
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    System.out.println(username + " is NOT authenticated");
                }
            }
        }

        return false;
    }

    public static boolean userMemberOf(DirContext ctx, String searchBase, HashMap processedUserGroups, HashMap unProcessedUserGroups, String groupCN, String groupDistinguishedName) throws NamingException {
        HashMap newUnProcessedGroups = new HashMap();
        for (Iterator entry = unProcessedUserGroups.keySet().iterator(); entry.hasNext();) {
            String unprocessedGroupDistinguishedName = (String) entry.next();
            String unprocessedGroupCN = (String) unProcessedUserGroups.get(unprocessedGroupDistinguishedName);
            if (processedUserGroups.get(unprocessedGroupDistinguishedName) != null) {
                System.out.println("Found  : " + unprocessedGroupDistinguishedName + " in processedGroups. skipping further processing of it...");
                // We already traversed this.
                continue;
            }
            if (isSame(groupCN, unprocessedGroupCN) && isSame(groupDistinguishedName, unprocessedGroupDistinguishedName)) {
                System.out.println("Found Match DistinguishedName : " + unprocessedGroupDistinguishedName + ", CN : " + unprocessedGroupCN);
                return true;
            }
        }

        for (Iterator entry = unProcessedUserGroups.keySet().iterator(); entry.hasNext();) {
            String unprocessedGroupDistinguishedName = (String) entry.next();
            String unprocessedGroupCN = (String) unProcessedUserGroups.get(unprocessedGroupDistinguishedName);

            processedUserGroups.put(unprocessedGroupDistinguishedName, unprocessedGroupCN);

            // Fetch Groups in unprocessedGroupCN and put them in newUnProcessedGroups
            NamingEnumeration ns = executeSearch(ctx, SearchControls.SUBTREE_SCOPE, searchBase,
                    MessageFormat.format(SEARCH_GROUP_BY_GROUP_CN, new Object[]{unprocessedGroupCN}),
                    new String[]{CN, DISTINGUISHED_NAME, MEMBER_OF});

            // Loop through the search results
            while (ns.hasMoreElements()) {
                SearchResult sr = (SearchResult) ns.next();

                // Make sure we're looking at correct distinguishedName, because we're querying by CN
                String userDistinguishedName = (sr.getAttributes().get(DISTINGUISHED_NAME) != null ? sr.getAttributes().get(DISTINGUISHED_NAME).get().toString() : "");   //<-- Para evitar que falle por null
                if (!isSame(unprocessedGroupDistinguishedName, userDistinguishedName)) {
                    System.out.println("Processing CN : " + unprocessedGroupCN + ", DN : " + unprocessedGroupDistinguishedName + ", Got DN : " + userDistinguishedName + ", Ignoring...");
                    continue;
                }

                System.out.println("Processing for memberOf CN : " + unprocessedGroupCN + ", DN : " + unprocessedGroupDistinguishedName);
                // Look for and process memberOf
                Attribute memberOf = sr.getAttributes().get(MEMBER_OF);
                if (memberOf != null) {
                    for (Enumeration e1 = memberOf.getAll(); e1.hasMoreElements();) {
                        String unprocessedChildGroupDN = e1.nextElement().toString();
                        String unprocessedChildGroupCN = getCN(unprocessedChildGroupDN);
                        System.out.println("Adding to List of un-processed groups : " + unprocessedChildGroupDN + ", CN : " + unprocessedChildGroupCN);
                        newUnProcessedGroups.put(unprocessedChildGroupDN, unprocessedChildGroupCN);
                    }
                }
            }
        }
        if (newUnProcessedGroups.size() == 0) {
            System.out.println("newUnProcessedGroups.size() is 0. returning false...");
            return false;
        }

        //  process unProcessedUserGroups
        return userMemberOf(ctx, searchBase, processedUserGroups, newUnProcessedGroups, groupCN, groupDistinguishedName);
    }

    private static NamingEnumeration executeSearch(DirContext ctx, int searchScope, String searchBase, String searchFilter, String[] attributes) throws NamingException {
        // Create the search controls
        SearchControls searchCtls = new SearchControls();

        // Specify the attributes to return
        if (attributes != null) {
            searchCtls.setReturningAttributes(attributes);
        }

        // Specify the search scope
        searchCtls.setSearchScope(searchScope);

        // Search for objects using the filter
        NamingEnumeration result = ctx.search(searchBase, searchFilter, searchCtls);
        return result;
    }

    private static SearchResult executeSearchSingleResult(DirContext ctx, int searchScope, String searchBase, String searchFilter, String[] attributes) throws NamingException {
        NamingEnumeration result = executeSearch(ctx, searchScope, searchBase, searchFilter, attributes);

        SearchResult sr = null;
        // Loop through the search results
        while (result.hasMoreElements()) {
            sr = (SearchResult) result.next();
            break;
        }
        return sr;
    }
}
