package com.ca.tongxunlu.contact_manger;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * @创建者: 李月
 * @创建时间: 2016-05-13 下午 8:42
 * @描述:
 * @版本: $Rev$
 * @更新者: $Author$
 * @更新时间: $Date$
 * @更新描述: 添加联系人
 */
public class ADContacts {
    public static boolean insert(Context context, String given_name, String mobile_number, String work_email) {
        try {
            ContentValues values = new ContentValues();

            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
            Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);

            // 向data表插入姓名数据
            if (given_name != "") {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, given_name);
                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
            }

            // 向data表插入电话数据
            if (mobile_number != "") {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile_number);
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
            }

            // 向data表插入Email数据
            if (work_email != "") {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Email.DATA, work_email);
                values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //删除联系人
    public static void deleteContact(Context context, String name) {
        //根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Contacts.Data._ID}, "display_name=?", new String[]{name}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            resolver.delete(uri, "display_name=?", new String[]{name});
            uri = Uri.parse("content://com.android.contacts/data");
            resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});
        }
    }
}
