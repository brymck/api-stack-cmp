/**
* Greeting
* This is a example OpenAPI spec to test performance implications of various protocols and server libraries 
*
* The version of the OpenAPI document: 0.0.1
* 
*
* NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
* https://openapi-generator.tech
* Do not edit the class manually.
*/
package com.github.brymck.greetings.models


import com.google.gson.annotations.SerializedName
/**
 * 
 * @param name The greeting's message
 */

data class Request (
    /* The greeting's message */
    @SerializedName("name")
    val name: kotlin.String
) 



