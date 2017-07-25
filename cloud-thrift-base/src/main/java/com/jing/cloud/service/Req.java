/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.jing.cloud.service;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2017-07-25")
public class Req implements org.apache.thrift.TBase<Req, Req._Fields>, java.io.Serializable, Cloneable, Comparable<Req> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Req");

  private static final org.apache.thrift.protocol.TField SERVICE_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("serviceName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField METHOD_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("methodName", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField JSON_PARAM_FIELD_DESC = new org.apache.thrift.protocol.TField("jsonParam", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField REQ_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("reqId", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField ROUTER_FIELD_DESC = new org.apache.thrift.protocol.TField("router", org.apache.thrift.protocol.TType.I64, (short)5);
  private static final org.apache.thrift.protocol.TField VERSION_FIELD_DESC = new org.apache.thrift.protocol.TField("version", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ReqStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ReqTupleSchemeFactory());
  }

  public String serviceName; // required
  public String methodName; // required
  public String jsonParam; // required
  public String reqId; // optional
  public long router; // optional
  public String version; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    SERVICE_NAME((short)1, "serviceName"),
    METHOD_NAME((short)2, "methodName"),
    JSON_PARAM((short)3, "jsonParam"),
    REQ_ID((short)4, "reqId"),
    ROUTER((short)5, "router"),
    VERSION((short)6, "version");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // SERVICE_NAME
          return SERVICE_NAME;
        case 2: // METHOD_NAME
          return METHOD_NAME;
        case 3: // JSON_PARAM
          return JSON_PARAM;
        case 4: // REQ_ID
          return REQ_ID;
        case 5: // ROUTER
          return ROUTER;
        case 6: // VERSION
          return VERSION;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __ROUTER_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.REQ_ID,_Fields.ROUTER,_Fields.VERSION};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SERVICE_NAME, new org.apache.thrift.meta_data.FieldMetaData("serviceName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.METHOD_NAME, new org.apache.thrift.meta_data.FieldMetaData("methodName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.JSON_PARAM, new org.apache.thrift.meta_data.FieldMetaData("jsonParam", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.REQ_ID, new org.apache.thrift.meta_data.FieldMetaData("reqId", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ROUTER, new org.apache.thrift.meta_data.FieldMetaData("router", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.VERSION, new org.apache.thrift.meta_data.FieldMetaData("version", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Req.class, metaDataMap);
  }

  public Req() {
  }

  public Req(
    String serviceName,
    String methodName,
    String jsonParam)
  {
    this();
    this.serviceName = serviceName;
    this.methodName = methodName;
    this.jsonParam = jsonParam;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Req(Req other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetServiceName()) {
      this.serviceName = other.serviceName;
    }
    if (other.isSetMethodName()) {
      this.methodName = other.methodName;
    }
    if (other.isSetJsonParam()) {
      this.jsonParam = other.jsonParam;
    }
    if (other.isSetReqId()) {
      this.reqId = other.reqId;
    }
    this.router = other.router;
    if (other.isSetVersion()) {
      this.version = other.version;
    }
  }

  public Req deepCopy() {
    return new Req(this);
  }

  @Override
  public void clear() {
    this.serviceName = null;
    this.methodName = null;
    this.jsonParam = null;
    this.reqId = null;
    setRouterIsSet(false);
    this.router = 0;
    this.version = null;
  }

  public String getServiceName() {
    return this.serviceName;
  }

  public Req setServiceName(String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  public void unsetServiceName() {
    this.serviceName = null;
  }

  /** Returns true if field serviceName is set (has been assigned a value) and false otherwise */
  public boolean isSetServiceName() {
    return this.serviceName != null;
  }

  public void setServiceNameIsSet(boolean value) {
    if (!value) {
      this.serviceName = null;
    }
  }

  public String getMethodName() {
    return this.methodName;
  }

  public Req setMethodName(String methodName) {
    this.methodName = methodName;
    return this;
  }

  public void unsetMethodName() {
    this.methodName = null;
  }

  /** Returns true if field methodName is set (has been assigned a value) and false otherwise */
  public boolean isSetMethodName() {
    return this.methodName != null;
  }

  public void setMethodNameIsSet(boolean value) {
    if (!value) {
      this.methodName = null;
    }
  }

  public String getJsonParam() {
    return this.jsonParam;
  }

  public Req setJsonParam(String jsonParam) {
    this.jsonParam = jsonParam;
    return this;
  }

  public void unsetJsonParam() {
    this.jsonParam = null;
  }

  /** Returns true if field jsonParam is set (has been assigned a value) and false otherwise */
  public boolean isSetJsonParam() {
    return this.jsonParam != null;
  }

  public void setJsonParamIsSet(boolean value) {
    if (!value) {
      this.jsonParam = null;
    }
  }

  public String getReqId() {
    return this.reqId;
  }

  public Req setReqId(String reqId) {
    this.reqId = reqId;
    return this;
  }

  public void unsetReqId() {
    this.reqId = null;
  }

  /** Returns true if field reqId is set (has been assigned a value) and false otherwise */
  public boolean isSetReqId() {
    return this.reqId != null;
  }

  public void setReqIdIsSet(boolean value) {
    if (!value) {
      this.reqId = null;
    }
  }

  public long getRouter() {
    return this.router;
  }

  public Req setRouter(long router) {
    this.router = router;
    setRouterIsSet(true);
    return this;
  }

  public void unsetRouter() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ROUTER_ISSET_ID);
  }

  /** Returns true if field router is set (has been assigned a value) and false otherwise */
  public boolean isSetRouter() {
    return EncodingUtils.testBit(__isset_bitfield, __ROUTER_ISSET_ID);
  }

  public void setRouterIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ROUTER_ISSET_ID, value);
  }

  public String getVersion() {
    return this.version;
  }

  public Req setVersion(String version) {
    this.version = version;
    return this;
  }

  public void unsetVersion() {
    this.version = null;
  }

  /** Returns true if field version is set (has been assigned a value) and false otherwise */
  public boolean isSetVersion() {
    return this.version != null;
  }

  public void setVersionIsSet(boolean value) {
    if (!value) {
      this.version = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case SERVICE_NAME:
      if (value == null) {
        unsetServiceName();
      } else {
        setServiceName((String)value);
      }
      break;

    case METHOD_NAME:
      if (value == null) {
        unsetMethodName();
      } else {
        setMethodName((String)value);
      }
      break;

    case JSON_PARAM:
      if (value == null) {
        unsetJsonParam();
      } else {
        setJsonParam((String)value);
      }
      break;

    case REQ_ID:
      if (value == null) {
        unsetReqId();
      } else {
        setReqId((String)value);
      }
      break;

    case ROUTER:
      if (value == null) {
        unsetRouter();
      } else {
        setRouter((Long)value);
      }
      break;

    case VERSION:
      if (value == null) {
        unsetVersion();
      } else {
        setVersion((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case SERVICE_NAME:
      return getServiceName();

    case METHOD_NAME:
      return getMethodName();

    case JSON_PARAM:
      return getJsonParam();

    case REQ_ID:
      return getReqId();

    case ROUTER:
      return getRouter();

    case VERSION:
      return getVersion();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case SERVICE_NAME:
      return isSetServiceName();
    case METHOD_NAME:
      return isSetMethodName();
    case JSON_PARAM:
      return isSetJsonParam();
    case REQ_ID:
      return isSetReqId();
    case ROUTER:
      return isSetRouter();
    case VERSION:
      return isSetVersion();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Req)
      return this.equals((Req)that);
    return false;
  }

  public boolean equals(Req that) {
    if (that == null)
      return false;

    boolean this_present_serviceName = true && this.isSetServiceName();
    boolean that_present_serviceName = true && that.isSetServiceName();
    if (this_present_serviceName || that_present_serviceName) {
      if (!(this_present_serviceName && that_present_serviceName))
        return false;
      if (!this.serviceName.equals(that.serviceName))
        return false;
    }

    boolean this_present_methodName = true && this.isSetMethodName();
    boolean that_present_methodName = true && that.isSetMethodName();
    if (this_present_methodName || that_present_methodName) {
      if (!(this_present_methodName && that_present_methodName))
        return false;
      if (!this.methodName.equals(that.methodName))
        return false;
    }

    boolean this_present_jsonParam = true && this.isSetJsonParam();
    boolean that_present_jsonParam = true && that.isSetJsonParam();
    if (this_present_jsonParam || that_present_jsonParam) {
      if (!(this_present_jsonParam && that_present_jsonParam))
        return false;
      if (!this.jsonParam.equals(that.jsonParam))
        return false;
    }

    boolean this_present_reqId = true && this.isSetReqId();
    boolean that_present_reqId = true && that.isSetReqId();
    if (this_present_reqId || that_present_reqId) {
      if (!(this_present_reqId && that_present_reqId))
        return false;
      if (!this.reqId.equals(that.reqId))
        return false;
    }

    boolean this_present_router = true && this.isSetRouter();
    boolean that_present_router = true && that.isSetRouter();
    if (this_present_router || that_present_router) {
      if (!(this_present_router && that_present_router))
        return false;
      if (this.router != that.router)
        return false;
    }

    boolean this_present_version = true && this.isSetVersion();
    boolean that_present_version = true && that.isSetVersion();
    if (this_present_version || that_present_version) {
      if (!(this_present_version && that_present_version))
        return false;
      if (!this.version.equals(that.version))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_serviceName = true && (isSetServiceName());
    list.add(present_serviceName);
    if (present_serviceName)
      list.add(serviceName);

    boolean present_methodName = true && (isSetMethodName());
    list.add(present_methodName);
    if (present_methodName)
      list.add(methodName);

    boolean present_jsonParam = true && (isSetJsonParam());
    list.add(present_jsonParam);
    if (present_jsonParam)
      list.add(jsonParam);

    boolean present_reqId = true && (isSetReqId());
    list.add(present_reqId);
    if (present_reqId)
      list.add(reqId);

    boolean present_router = true && (isSetRouter());
    list.add(present_router);
    if (present_router)
      list.add(router);

    boolean present_version = true && (isSetVersion());
    list.add(present_version);
    if (present_version)
      list.add(version);

    return list.hashCode();
  }

  @Override
  public int compareTo(Req other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetServiceName()).compareTo(other.isSetServiceName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetServiceName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.serviceName, other.serviceName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMethodName()).compareTo(other.isSetMethodName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMethodName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.methodName, other.methodName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetJsonParam()).compareTo(other.isSetJsonParam());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetJsonParam()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.jsonParam, other.jsonParam);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetReqId()).compareTo(other.isSetReqId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetReqId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.reqId, other.reqId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRouter()).compareTo(other.isSetRouter());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRouter()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.router, other.router);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetVersion()).compareTo(other.isSetVersion());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVersion()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.version, other.version);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Req(");
    boolean first = true;

    sb.append("serviceName:");
    if (this.serviceName == null) {
      sb.append("null");
    } else {
      sb.append(this.serviceName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("methodName:");
    if (this.methodName == null) {
      sb.append("null");
    } else {
      sb.append(this.methodName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("jsonParam:");
    if (this.jsonParam == null) {
      sb.append("null");
    } else {
      sb.append(this.jsonParam);
    }
    first = false;
    if (isSetReqId()) {
      if (!first) sb.append(", ");
      sb.append("reqId:");
      if (this.reqId == null) {
        sb.append("null");
      } else {
        sb.append(this.reqId);
      }
      first = false;
    }
    if (isSetRouter()) {
      if (!first) sb.append(", ");
      sb.append("router:");
      sb.append(this.router);
      first = false;
    }
    if (isSetVersion()) {
      if (!first) sb.append(", ");
      sb.append("version:");
      if (this.version == null) {
        sb.append("null");
      } else {
        sb.append(this.version);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ReqStandardSchemeFactory implements SchemeFactory {
    public ReqStandardScheme getScheme() {
      return new ReqStandardScheme();
    }
  }

  private static class ReqStandardScheme extends StandardScheme<Req> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Req struct) throws TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // SERVICE_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.serviceName = iprot.readString();
              struct.setServiceNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // METHOD_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.methodName = iprot.readString();
              struct.setMethodNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // JSON_PARAM
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.jsonParam = iprot.readString();
              struct.setJsonParamIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // REQ_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.reqId = iprot.readString();
              struct.setReqIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // ROUTER
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.router = iprot.readI64();
              struct.setRouterIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // VERSION
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.version = iprot.readString();
              struct.setVersionIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Req struct) throws TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.serviceName != null) {
        oprot.writeFieldBegin(SERVICE_NAME_FIELD_DESC);
        oprot.writeString(struct.serviceName);
        oprot.writeFieldEnd();
      }
      if (struct.methodName != null) {
        oprot.writeFieldBegin(METHOD_NAME_FIELD_DESC);
        oprot.writeString(struct.methodName);
        oprot.writeFieldEnd();
      }
      if (struct.jsonParam != null) {
        oprot.writeFieldBegin(JSON_PARAM_FIELD_DESC);
        oprot.writeString(struct.jsonParam);
        oprot.writeFieldEnd();
      }
      if (struct.reqId != null) {
        if (struct.isSetReqId()) {
          oprot.writeFieldBegin(REQ_ID_FIELD_DESC);
          oprot.writeString(struct.reqId);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetRouter()) {
        oprot.writeFieldBegin(ROUTER_FIELD_DESC);
        oprot.writeI64(struct.router);
        oprot.writeFieldEnd();
      }
      if (struct.version != null) {
        if (struct.isSetVersion()) {
          oprot.writeFieldBegin(VERSION_FIELD_DESC);
          oprot.writeString(struct.version);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ReqTupleSchemeFactory implements SchemeFactory {
    public ReqTupleScheme getScheme() {
      return new ReqTupleScheme();
    }
  }

  private static class ReqTupleScheme extends TupleScheme<Req> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Req struct) throws TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetServiceName()) {
        optionals.set(0);
      }
      if (struct.isSetMethodName()) {
        optionals.set(1);
      }
      if (struct.isSetJsonParam()) {
        optionals.set(2);
      }
      if (struct.isSetReqId()) {
        optionals.set(3);
      }
      if (struct.isSetRouter()) {
        optionals.set(4);
      }
      if (struct.isSetVersion()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetServiceName()) {
        oprot.writeString(struct.serviceName);
      }
      if (struct.isSetMethodName()) {
        oprot.writeString(struct.methodName);
      }
      if (struct.isSetJsonParam()) {
        oprot.writeString(struct.jsonParam);
      }
      if (struct.isSetReqId()) {
        oprot.writeString(struct.reqId);
      }
      if (struct.isSetRouter()) {
        oprot.writeI64(struct.router);
      }
      if (struct.isSetVersion()) {
        oprot.writeString(struct.version);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Req struct) throws TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.serviceName = iprot.readString();
        struct.setServiceNameIsSet(true);
      }
      if (incoming.get(1)) {
        struct.methodName = iprot.readString();
        struct.setMethodNameIsSet(true);
      }
      if (incoming.get(2)) {
        struct.jsonParam = iprot.readString();
        struct.setJsonParamIsSet(true);
      }
      if (incoming.get(3)) {
        struct.reqId = iprot.readString();
        struct.setReqIdIsSet(true);
      }
      if (incoming.get(4)) {
        struct.router = iprot.readI64();
        struct.setRouterIsSet(true);
      }
      if (incoming.get(5)) {
        struct.version = iprot.readString();
        struct.setVersionIsSet(true);
      }
    }
  }

}
