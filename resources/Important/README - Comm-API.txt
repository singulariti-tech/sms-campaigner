URL - Java(tm) Communication API  - https://cds.sun.com/is-bin/INTERSHOP.enfinity/WFS/CDS-CDS_SMI-Site/en_US/-/USD/ViewFilteredProducts-SingleVariationTypeFilter

COMM API is a Java extension providing access to RS-232 serial ports and IEEE-1284 parallel ports (SPP mode). Sun Ray platform support is provided. Documentation and sample code are included, as well as interactive test utilities. API serial features provide access to: 

* Port settings (baud rate, parity, stop bits) 
* Port naming, mapping, enumeration (configurable) 
* Data transfer over TX, RX lines 
* DTR, CD, CTS, RTS and DSR signals 
* Hardware/Software flow-control 
* Receive-buffer threshold control 

Asynchronous events indicating: 
* Data available on an RS-232 port 
* Port hardware line level changes 

* Port ownership changes within a JVM Update History: 
* javax.comm 3.0, Update 1 o Fixed problems locating and parsing portmap.conf 
* javax.comm 3.0, FCS o Redesigned portmapping and Sun Ray interoperability (see bundled documentation) 

* 2.0.3: o Added portmapping (comm.jar available 'as-is' for 3rd party native lib support)
* Several Bugfixes to 2.0.3 (see bundled release notes) 

Note: Sun no longer offer's the Windows platform binaries of javax.comm, however javax.comm 2.0.3 can be used for the Windows platform, by using it in conjunction with the Win32 implementation layer provided by the RxTx project. To use that, download javax.comm for the 'generic' platform (which provides the front-end javax.comm API only, without platform specific back-end implementations bundled). Then acquire the Windows binary implementation rxtx-2.0.7pre1 from http://www.rxtx.org.