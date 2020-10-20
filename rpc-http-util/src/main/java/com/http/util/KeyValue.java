package com.http.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.http.NameValuePair;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KeyValue implements NameValuePair
{
    String name;
    String value;
}
